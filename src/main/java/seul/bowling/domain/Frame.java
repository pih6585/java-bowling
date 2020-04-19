package seul.bowling.domain;

import lombok.Getter;
import seul.bowling.domain.status.Status;

import java.util.Objects;

@Getter
public class Frame {
    protected static final int ZERO = 0;
    private static final int ONE = 1;
    public static final int LAST_FRAME_INDEX = 9;

    private int index;
    @Getter
    protected Status status;

    protected Frame(int index) {
        this.index = index;
        this.status = new Status();
    }

    public void addPins(int clearPin) {
        addPins(clearPin, false);
    }

    void addPins(int clearPin, boolean isBonusPlay) {
        status.addPins(clearPin, isBonusPlay);

        if (!this.status.endJudgmentStatus()) {
            this.status = status.judgmentStatus(status.pinsAllClear());
            this.status.addScore(clearPin);
        }
    }

    public void addBonusScore(int bonusScore) {
        status.addBonusScore(bonusScore);
    }

    public void addCumulativeScore(int score) {
        status.addCumulativeScore(score);
    }

    public Frame next() {
        int newIndex = index + ONE;
        if (newIndex == LAST_FRAME_INDEX) {
            return new LastFrame(newIndex);
        }

        return new Frame(newIndex);
    }

    public static Frame first() {
        return new Frame(ZERO);
    }

    public boolean endFrame() {
        return status.endJudgmentStatus();
    }

    public boolean endCalculateScore() {
        return status.endScore();
    }

    public boolean availableAddBonusScore() {
        return status.availableAddBonusScore();
    }

    public boolean isLastFame() {
        return false;
    }

    public int getScore() {
        return this.status.getToTalScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Frame frame = (Frame) o;
        return index == frame.index;
    }

    @Override
    public int hashCode() {
        return Objects.hash(index);
    }
}