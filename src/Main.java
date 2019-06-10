import core.Game;
import manager.DisplayManager;
import EventRecorder.*;

import java.awt.*;

/**
 * FightingICEのメインメソッドを持つクラス．
 */
public class Main {

	/**
	 * 起動時に入力した引数に応じて起動情報を設定し, それを基にゲームを開始する．<br>
	 * このメソッドはFightingICEのメインメソッドである．
	 *
	 * @param options
	 *            起動時に入力した全ての引数を格納した配列
	 */
	public static void main(String[] options) {
		Game game = new Game();
		options = new String[2];
		options[0]="--mute";
		//options[1]="--fastmode";
		options[1]="--json";
		game.setOptions(options);
		DisplayManager displayManager = new DisplayManager();
        //start
		//start2
		// ゲームの開始
		displayManager.start(game);
		//
		EventRecorder ev = new EventRecorder();
		ev.start();
	}
}
