package com.github.rthoth.xysplit;

import java.util.Map;
import java.util.NavigableMap;

public abstract class RangeMap<T> {

	protected final NavigableMap<Double, Range<T>> underlying;

	public RangeMap(NavigableMap<Double, Range<T>> underlying) {
		this.underlying = underlying;
	}

	public boolean intersects(double start, double stop) {
		assert start < stop;
		Map.Entry<Double, Range<T>> entry = underlying.lowerEntry(stop);
		return entry != null && entry.getValue().stop > start;
	}

	public T get(double position) {
		Map.Entry<Double, Range<T>> entry = underlying.floorEntry(position);

		if (entry != null) {
			Range<T> range = entry.getValue();
			return range.start <= position && range.stop >= position ? range.value : null;
		} else
			return null;
	}

	public void add(double start, double stop, T value) {
		if (!intersects(start, stop))
			underlying.put(start, new Range<>(start, stop, value));
	}

	public static class Range<T> {

		public final double start;
		public final double stop;
		public final T value;

		public Range(double start, double stop, T value) {
			this.start = start;
			this.stop = stop;
			this.value = value;
		}
	}

	public static class TreeMap<T> extends RangeMap<T> {

		public TreeMap() {
			super(new java.util.TreeMap<>());
		}
	}
}