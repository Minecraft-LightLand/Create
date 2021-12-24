package com.simibubi.create.foundation.advancement;

import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.Function;

@ParametersAreNonnullByDefault
public class SimpleStringSerializableTrigger<T> extends StringSerializableTrigger<T>{

	private final Function<String, T> decode;
	private final Function<T, String> encode;

	protected SimpleStringSerializableTrigger(String id, Function<String, T> decode, Function<T, String> encode) {
		super(id);
		this.decode = decode;
		this.encode = encode;
	}

	@Nullable
	@Override
	protected T getValue(String key) {
		return decode.apply(key);
	}

	@Nullable
	@Override
	protected String getKey(T value) {
		return encode.apply(value);
	}
}
