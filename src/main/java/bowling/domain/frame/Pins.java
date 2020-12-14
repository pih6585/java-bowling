package bowling.domain.frame;

import bowling.dto.PinsDto;
import bowling.exception.PinsOutOfRangeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

class Pins {
    // NOTE: get 에서의 bigO 를 1로 보장하기 위해, ArrayList 사용
    private final List<Pin> pins = new ArrayList<>();

    public void add(Pin pin) {
        pins.add(pin);
    }

    int size() {
        return pins.size();
    }

    int sum(int startIdx, int offset) {
        int endIdx = startIdx + offset;
        boolean isOutOfRange = startIdx < 0 || offset < 0 || endIdx > pins.size();
        if (isOutOfRange) {
            throw new PinsOutOfRangeException("pins 의 범위를 벗어난 index 입니다.");
        }
        return IntStream.range(startIdx, endIdx)
                .mapToObj(pins::get)
                .reduce(0, (acc, pin) -> pin.sum(acc), Integer::sum);
    }

    PinsDto exportPinsDto() {
        return pins.stream()
                .map(Pin::exportPinDto)
                .collect(collectingAndThen(toList(), PinsDto::new));
    }
}
