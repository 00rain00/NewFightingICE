package fighting;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.LinkedList;

import org.lwjgl.BufferUtils;

import input.KeyData;
import setting.GameSetting;
import struct.CharacterData;
import struct.FrameData;

public class Fighting {

	private Character[] playerCharacters;

	private Deque<LoopEffect> projectileDeque;

	private Deque<KeyData> inputCommands;

	private BufferedImage screen;

	public Fighting() {
		this.playerCharacters = new Character[2];
		this.projectileDeque = new LinkedList<LoopEffect>();
		this.inputCommands = new LinkedList<KeyData>();
		this.screen = null;

	}

	public void initialize() {

		for(int i = 0; i < 2; i++){
			this.playerCharacters[i] = new Character();
			//this.playerCharacters[i].initialize(初期化);
		}

		this.screen = new BufferedImage(GameSetting.STAGE_WIDTH, GameSetting.STAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);
		this.projectileDeque = new LinkedList<LoopEffect>();
		this.inputCommands = new LinkedList<KeyData>();

		///// 旧Fighting処理内容/////

		// BGMのロード
		// SEロード
		// 画像系ロード←Launcherでやってる
		// スクリーン画像取得←ここでやる
		// 背景画像ロード←Launcherでやってる
		// スコア・経過時間の結果を格納する配列初期化←Playでやってる
		// 波動拳格納リスト初期化←ここ
	 // コマンド格納リスト初期化←ここ
		// リプレイ用ファイルオープン←ここかPlay
		// Json用ファイルオープン←未定

	}

	public void processingFight(int nowFrame, KeyData keyData) {
		//1. キャラクターの状態の更新
		//2. コマンドの実行・対戦処理
		//3. 当たり判定の処理
		//4. 攻撃パラメータの更新
		//5. キャラクター情報の更新

	}

	public Character[] getCharacters() {
		return this.playerCharacters.clone();
	}

	/**
	 * 現在のフレームにおけるゲーム情報を格納したフレームデータを作成する<br>
	 * 両キャラクターの情報, 現在のフレーム数, 現在のラウンド, 波動拳の情報を格納したリスト, 両キャラクターのキー情報, 画面のピクセル情報,
	 * 画面のBufferedImage
	 */
	public FrameData createFrameData(int nowFrame, int round, KeyData keyData) {
		CharacterData[] characterData = new CharacterData[] { new CharacterData(playerCharacters[0]),
				new CharacterData(playerCharacters[1]) };

		Deque<Attack> newAttackDeque = new LinkedList<Attack>();
		for (LoopEffect loopEffect : this.projectileDeque) {
			// newAttackDeque.addLast(loopEffect.getAttack());
		}

		return new FrameData(characterData, nowFrame, round, newAttackDeque, keyData, getDisplayByteBuffer(),
				this.screen);
	}

	public void initRound() {

	}

	/**
	 * Obtain RGB data of the screen in the form of ByteBuffer Warning: If the
	 * window is disabled, will just returns a black buffer
	 *
	 * @return RGB data of the screen in the form of ByteBuffer
	 */
	private ByteBuffer getDisplayByteBuffer() {
		// Allocate memory for the RGB data of the screen
		ByteBuffer pixels = BufferUtils.createByteBuffer(3 * GameSetting.STAGE_WIDTH * GameSetting.STAGE_HEIGHT);
		pixels.clear();

		// Assign the RGB data of the screen to pixels, a ByteBuffer
		// variable
		glReadPixels(0, 0, GameSetting.STAGE_WIDTH, GameSetting.STAGE_HEIGHT, GL_RGB, GL_UNSIGNED_BYTE, pixels);
		pixels.rewind();

		return pixels;
	}
}
