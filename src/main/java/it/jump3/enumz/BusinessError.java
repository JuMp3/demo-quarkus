package it.jump3.enumz;

import java.util.stream.Stream;

public enum BusinessError {

    // COMMON
    IB_400(400),
    IB_401(401),
    IB_403(403),
    IB_500(500),
    IB_503(503),
    IB_504(504),

    IB_400_USER(4001),
    IB_404_USER(4041),
    IB_409_USER(4091);

    private final int code;

    BusinessError(int c) {
        code = c;
    }

    public int code() {
        return code;
    }

    public static BusinessError fromCode(int code) {
        return Stream.of(BusinessError.values())
                .filter(businessError -> businessError.code() == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unexpected BusinessError id: ".concat(String.valueOf(code))));
    }


    @Override
    public String toString() {
        return "GP-".concat(String.valueOf(code()));
    }
}
