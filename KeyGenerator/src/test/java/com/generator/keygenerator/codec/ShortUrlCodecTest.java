package com.generator.keygenerator.codec;

import com.generator.keygenerator.codec.exception.UnknownStrategyException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShortUrlCodecTest {

    @InjectMocks
    private ShortUrlCodec shortUrlCodec;
    @Mock
    private Map<String, CodecStrategy> codecStrategies;

    @Test
    void encode_unknown() {
        String codecStrategyName = "Base62Codec";
        Throwable thrown = catchThrowable(() -> shortUrlCodec.encode(codecStrategyName, 0L));
        Assertions.assertThat(thrown)
                .isInstanceOf(UnknownStrategyException.class)
                .hasMessage("Unknown codec strategy: " + codecStrategyName);

    }

    @Test
    void encode_known() {
        String codecStrategyName = "Base58Codec";
        Base58Codec mockCodec = mock(Base58Codec.class);
        when(mockCodec.encode(anyLong())).thenReturn("success");
        when(codecStrategies.get(codecStrategyName)).thenReturn(mockCodec);

        Assertions.assertThat(shortUrlCodec.encode(codecStrategyName, 0L)).isEqualTo("success");
    }
}
