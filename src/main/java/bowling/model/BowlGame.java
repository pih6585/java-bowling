package bowling.model;

import java.util.ArrayList;
import java.util.Objects;

public class BowlGame {

	private static final String LIMIT_MAX_PIN_ERROR_MESSAGE = "최대 쓰러뜨릴 수 있는 수가 초과 하였습니다.";
	private static final int MAX_RESULT_SIZE = 3;
	private static final int MAX_ROUND = 10;
	private static final int MAX_PIN = 10;

	private final int frameNumber;
	private final GameResult gameResult;

	public BowlGame(int frameNumber) {
		this.frameNumber = frameNumber;
		this.gameResult = new GameResult(new ArrayList<>(MAX_RESULT_SIZE));
	}

	public GameResult play(Pin pin) {
		checkNormalScore(pin);
		checkFinalScore(pin);
		gameResult.save(pin);
		return gameResult;
	}

	private void checkNormalScore(Pin pin) {
		if (frameNumber < MAX_ROUND && (gameResult.findTotalScore() + pin.getPin()) > MAX_PIN) {
			throw new IllegalArgumentException(LIMIT_MAX_PIN_ERROR_MESSAGE);
		}
	}

	private void checkFinalScore(Pin pin) {
		if (frameNumber == MAX_ROUND && (gameResult.isStrikeOrSpare() && pin.getPin() > MAX_PIN)) {
			throw new IllegalArgumentException(LIMIT_MAX_PIN_ERROR_MESSAGE);
		}
		if (frameNumber == MAX_ROUND && (gameResult.isNotStrikeOrSpare()
			&& (gameResult.findTotalScore() + pin.getPin()) > MAX_PIN)) {
			throw new IllegalArgumentException(LIMIT_MAX_PIN_ERROR_MESSAGE);
		}
	}

	public boolean isStrike() {
		return gameResult.isStrike();
	}

	public boolean isNotStrikeOrSpare() {
		return gameResult.isNotStrikeOrSpare();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		BowlGame bowlGame = (BowlGame)o;
		return frameNumber == bowlGame.frameNumber && Objects.equals(gameResult, bowlGame.gameResult);
	}

	@Override
	public int hashCode() {
		return Objects.hash(frameNumber, gameResult);
	}

}