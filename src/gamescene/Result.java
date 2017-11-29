package gamescene;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import enumerate.GameSceneName;
import informationcontainer.RoundResult;
import input.Keyboard;
import manager.GraphicManager;
import manager.InputManager;
import setting.GameSetting;
import util.LogWriter;

public class Result extends GameScene {

	private ArrayList<RoundResult> roundResults;

	private  String timeInfo;

	public Result() {
		this.roundResults = new ArrayList<RoundResult>();
		this.timeInfo = "0";
	}

	public Result(ArrayList<RoundResult> roundResults, String timeInfo) {
		this.roundResults = new ArrayList<RoundResult>(roundResults);
		this.timeInfo = timeInfo;
		roundResults.clear();
	}

	@Override
	public void initialize() {
		InputManager.getInstance().setSceneName(GameSceneName.RESULT);
		LogWriter.getInstance().outputResult(this.roundResults, LogWriter.CSV, this.timeInfo);
	}

	@Override
	public void update() {
		int[] positionX = new int[] { GameSetting.STAGE_WIDTH / 2 - 70, GameSetting.STAGE_WIDTH / 2 + 10 };

		for (int i = 0; i < this.roundResults.size(); i++) {
			String[] score = new String[] { String.valueOf(this.roundResults.get(i).getRemainingHPs()[0]),
					String.valueOf(this.roundResults.get(i).getRemainingHPs()[1]) };

			//スコアの描画
			GraphicManager.getInstance().drawString(score[0], positionX[0], 50 + i * 100);
			GraphicManager.getInstance().drawString(score[1], positionX[1], 50 + i * 100);

			//勝ちや引き分けに応じてWin !やDrawをスコアの横に印字
			switch (getWinPlayer(i)) {
			case 1:
				GraphicManager.getInstance().drawString("Win !", positionX[0] - 100, 50 + i * 100);
				break;
			case -1:
				GraphicManager.getInstance().drawString("Win !", positionX[1] + 80, 50 + i * 100);
				break;
			default:
				GraphicManager.getInstance().drawString("Draw", positionX[0] - 100, 50 + i * 100);
				GraphicManager.getInstance().drawString("Draw", positionX[1] + 80, 50 + i * 100);
				break;
			}
		}

		String string = "Press Enter key to return menu";
		GraphicManager.getInstance().drawString(string, GameSetting.STAGE_WIDTH / 2 - string.length() * 5 - 30, 400);

		if (Keyboard.getKeyDown(GLFW_KEY_ENTER)) {
			HomeMenu homeMenu = new HomeMenu();
			this.setTransitionFlag(true); // 現在のシーンからの遷移要求をtrueに
			this.setNextGameScene(homeMenu); // 次のシーンをセットする
		}
	}

	@Override
	public void close() {
		this.roundResults.clear();
	}

	/**
	 * どちらがそのラウンドで勝ったかを返す
	 *
	 * @return 0: 引き分け, 1: P1の勝ち, -1: P2の勝ち
	 */
	private int getWinPlayer(int i) {
		int[] remainingHPs = this.roundResults.get(i).getRemainingHPs();

		if (remainingHPs[0] == remainingHPs[1]) {
			return 0;
		} else if (remainingHPs[0] > remainingHPs[1]) {
			return 1;
		} else {
			return -1;
		}
	}
}
