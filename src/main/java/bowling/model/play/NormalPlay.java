package bowling.model.play;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import bowling.model.Pin;
import bowling.model.score.Score;

public class NormalPlay implements Playable {

	private static final String LIMIT_MAX_PIN_ERROR_MESSAGE = "최대 쓰러뜨릴 수 있는 수가 초과 하였습니다.";
	private static final int MIN_PLAY_COUNT = 1;
	private static final int MAX_PLAY_COUNT = 2;
	private static final int FIRST_INDEX = 0;
	private static final int SECOND_INDEX = 1;

	private final int frameNumber;
	private final List<Pin> gameResult;

	public NormalPlay(int frameNumber) {
		this.frameNumber = frameNumber;
		this.gameResult = new ArrayList<>();
	}

	@Override
	public Score play(Pin pin) {
		checkScore(pin);
		gameResult.add(pin);
		return createScore();
	}

	private Score createScore() {
		if (isStrike()) {
			return Score.strike(gameResult);
		}
		if (isSpare()) {
			return Score.spare(gameResult);
		}
		if (isGameEnd()) {
			return Score.miss(gameResult, gameResult.stream()
				.mapToInt(Pin::getPin)
				.sum());
		}
		return Score.nothing(gameResult);
	}

	@Override
	public Pin findPin(int index) {
		return gameResult.get(index);
	}

	@Override
	public int countGame() {
		return gameResult.size();
	}

	private void checkScore(Pin pin) {
		if (gameResult.size() > 0 && findPin(FIRST_INDEX).isOverPin(pin)) {
			throw new IllegalArgumentException(LIMIT_MAX_PIN_ERROR_MESSAGE);
		}
	}

	@Override
	public boolean isStrike() {
		if (gameResult.size() == 0) {
			return false;
		}
		return gameResult.get(0).isMaxPin();
	}

	@Override
	public boolean isSpare() {
		if (isStrike()) {
			return false;
		}
		if (gameResult.size() < MAX_PLAY_COUNT) {
			return false;
		}
		return findPin(FIRST_INDEX).add(findPin(SECOND_INDEX)).isMaxPin();
	}

	@Override
	public boolean isMiss() {
		if (isStrike()) {
			return false;
		}
		if (gameResult.size() < MAX_PLAY_COUNT) {
			return false;
		}
		return !findPin(FIRST_INDEX).add(findPin(SECOND_INDEX)).isMaxPin();
	}

	@Override
	public boolean isGameEnd() {
		return (isStrike() || gameResult.size() > MIN_PLAY_COUNT);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		NormalPlay normalPlay = (NormalPlay)o;
		return frameNumber == normalPlay.frameNumber && Objects.equals(gameResult, normalPlay.gameResult);
	}

	@Override
	public int hashCode() {
		return Objects.hash(frameNumber, gameResult);
	}
}