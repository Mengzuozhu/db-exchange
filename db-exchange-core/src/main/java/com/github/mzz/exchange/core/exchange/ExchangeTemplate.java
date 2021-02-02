package com.github.mzz.exchange.core.exchange;

import com.github.mzz.exchange.core.reader.DataReader;
import com.github.mzz.exchange.core.writer.DataWriter;
import com.github.mzz.exchange.core.model.DataContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.exception.ExceptionUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.SynchronousSink;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author mengzz
 * @since 2020/12/30
 **/
@Slf4j
public class ExchangeTemplate<T> {
    private DataReader<?> dataReader;
    private DataWriter<T> dataWriter;
    private Flux<DataContext<T>> dataFlux;
    private CountDownLatch stateLatch = new CountDownLatch(1);
    private boolean hasBegin;

    private ExchangeTemplate(DataReader<?> dataReader, Flux<DataContext<T>> dataFlux) {
        this.dataReader = dataReader;
        this.dataFlux = dataFlux;
    }

    public static <T> ExchangeTemplate<T> from(DataReader<T> dataReader) {
        Validate.isTrue(dataReader != null, "the parameter dataReader require not null");
        return new ExchangeTemplate<>(dataReader, Flux.generate(sink -> read(sink, dataReader)));
    }

    public <V> ExchangeTemplate<V> map(Function<? super DataContext<T>, ? extends DataContext<V>> mapper) {
        return new ExchangeTemplate<>(dataReader, dataFlux.map(mapper));
    }

    public ExchangeTemplate<T> filter(Predicate<? super DataContext<T>> filter) {
        dataFlux = dataFlux.filter(filter);
        return this;
    }

    public ExchangeTemplate<T> scheduler(Scheduler scheduler) {
        dataFlux = dataFlux.subscribeOn(scheduler);
        return this;
    }

    public ExchangeWait execute(DataWriter<T> dataWriter) {
        this.dataWriter = dataWriter;
        begin();
        dataFlux.buffer(dataReader.getBatchSize())
                .doOnComplete(this::finish)
                .doOnError(this::onError)
                .subscribe(this::write, ExceptionUtils::rethrow);
        return ExchangeWait.of(stateLatch);
    }

    private static <T> void read(SynchronousSink<DataContext<T>> sink, DataReader<T> dataReader) {
        DataContext<T> context = dataReader.read();
        if (context == null) {
            sink.complete();
            return;
        }
        sink.next(context);
        if (context.isComplete()) {
            sink.complete();
        }
    }

    private void write(List<DataContext<T>> record) {
        dataWriter.write(record);
    }

    private void begin() {
        dataReader.begin();
        dataWriter.begin();
        hasBegin = true;
    }

    private void finish() {
        if (hasBegin) {
            dataReader.finish();
            dataWriter.finish();
        }
        stateLatch.countDown();
    }

    private void onError(Throwable e) {
        finish();
        log.debug("ExchangeTemplate execute fail", e);
    }

}
