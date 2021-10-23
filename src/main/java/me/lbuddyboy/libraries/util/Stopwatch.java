//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package me.lbuddyboy.libraries.util;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Ticker;

import java.util.concurrent.TimeUnit;

@Beta
@GwtCompatible(
    emulated = true
)
public final class Stopwatch {
    private final Ticker ticker;
    private boolean isRunning;
    private long elapsedNanos;
    private long startTick;

    public Stopwatch() {
        this(Ticker.systemTicker());
    }

    public Stopwatch(Ticker ticker) {
        this.ticker = (Ticker) Preconditions.checkNotNull(ticker);
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public Stopwatch start() {
        Preconditions.checkState(!this.isRunning);
        this.isRunning = true;
        this.startTick = this.ticker.read();
        return this;
    }

    public Stopwatch stop() {
        long tick = this.ticker.read();
        Preconditions.checkState(this.isRunning);
        this.isRunning = false;
        this.elapsedNanos += tick - this.startTick;
        return this;
    }

    public Stopwatch reset() {
        this.elapsedNanos = 0L;
        this.isRunning = false;
        return this;
    }

    private long elapsedNanos() {
        return this.isRunning ? this.ticker.read() - this.startTick + this.elapsedNanos : this.elapsedNanos;
    }

    public long elapsedTime(TimeUnit desiredUnit) {
        return desiredUnit.convert(this.elapsedNanos(), TimeUnit.NANOSECONDS);
    }

    public long elapsedMillis() {
        return this.elapsedTime(TimeUnit.MILLISECONDS);
    }

    @Override
    @GwtIncompatible("String.format()")
    public String toString() {
        return this.toString(4);
    }

    @GwtIncompatible("String.format()")
    public String toString(int significantDigits) {
        long nanos = this.elapsedNanos();
        TimeUnit unit = chooseUnit(nanos);
        double value = (double)nanos / (double)TimeUnit.NANOSECONDS.convert(1L, unit);
        return String.format("%." + significantDigits + "g %s", value, abbreviate(unit));
    }

    private static TimeUnit chooseUnit(long nanos) {
        if (TimeUnit.SECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.SECONDS;
        } else if (TimeUnit.MILLISECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L) {
            return TimeUnit.MILLISECONDS;
        } else {
            return TimeUnit.MICROSECONDS.convert(nanos, TimeUnit.NANOSECONDS) > 0L ? TimeUnit.MICROSECONDS : TimeUnit.NANOSECONDS;
        }
    }

    private static String abbreviate(TimeUnit unit) {
        switch(unit) {
        case NANOSECONDS:
            return "ns";
        case MICROSECONDS:
            return "μs";
        case MILLISECONDS:
            return "ms";
        case SECONDS:
            return "s";
        default:
            throw new AssertionError();
        }
    }
}
