package util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.Deque;
import java.util.LinkedList;

import fighting.Attack;
import fighting.Character;
import fighting.HitEffect;
import fighting.LoopEffect;
import image.Image;
import manager.GraphicManager;
import setting.FlagSetting;
import setting.GameSetting;
import setting.LaunchSetting;
import struct.HitArea;

public class ResourceDrawer {

	private static ResourceDrawer resourceDrawer = new ResourceDrawer();

	private ResourceDrawer() {
		System.out.println("Create instance: " + ResourceDrawer.class.getName());
	}

	public static ResourceDrawer getInstance() {
		return resourceDrawer;
	}

	public void drawResource(Character[] characters, Deque<LoopEffect> projectiles,
			LinkedList<LinkedList<HitEffect>> hitEffects, BufferedImage screen, int remainingTime, int round) {

		Graphics2D screenGraphic = screen.createGraphics();

		drawBackGroundImage(screenGraphic);

		drawCharacterImage(characters, screenGraphic);

		drawAttackImage(projectiles, screenGraphic);

		drawHPGaugeImage(characters);

		drawEnergyGaugeImage(characters);

		drawTimeImage(remainingTime);

		drawRoundNumber(round);

		drawHitCounter(characters);

		drawHitArea(characters, projectiles);

		drawHitEffects(hitEffects, screenGraphic);

		screenGraphic.dispose();
	}

	/**
	 *
	 * Draws the background image.
	 *
	 * @param screenGraphic
	 *            the screen graphics
	 *
	 */
	public void drawBackGroundImage(Graphics2D screenGraphic) {
		Image bg = GraphicManager.getInstance().getBackgroundImage().get(0);
		screenGraphic.drawImage(bg.getBufferedImage(), 0, 0, GameSetting.STAGE_WIDTH, GameSetting.STAGE_HEIGHT,
				Color.black, null);

		GraphicManager.getInstance().drawImage(bg, 0, 0, GameSetting.STAGE_WIDTH, GameSetting.STAGE_HEIGHT,
				Image.DIRECTION_RIGHT);
	}

	/**
	 * Draws both characters' images.
	 *
	 * @param screenGraphic
	 *            the screen graphics
	 */
	public void drawCharacterImage(Character[] playerCharacters, Graphics2D screenGraphic) {

		String[] names = { "P1", "P2" };

		// draw players name
		for (int i = 0; i < 2; ++i) {
			// Draw a character to match the direction
			BufferedImage image = playerCharacters[i].getNowImage().getBufferedImage();
			// キャラクターの向いている方向に応じて,画像を反転させる
			flipImage(image, playerCharacters[i].isFront());

			int positionX = playerCharacters[i].getHitAreaLeft()
					+ (playerCharacters[i].getHitAreaRight() - playerCharacters[i].getHitAreaLeft()) / 3;
			int positionY = playerCharacters[i].getHitAreaTop() - 50;

			GraphicManager.getInstance().drawString(names[i], positionX, positionY);

			screenGraphic.drawImage(image, playerCharacters[i].getX(), playerCharacters[i].getY(), playerCharacters[i].getGraphicSizeX(),
					playerCharacters[i].getGraphicSizeY(), null);

			GraphicManager.getInstance().drawImage(playerCharacters[i].getNowImage(), playerCharacters[i].getX(),
					playerCharacters[i].getY(), playerCharacters[i].getGraphicSizeX(),
					playerCharacters[i].getGraphicSizeY(), playerCharacters[i].isFront());
		}
	}

	/**
	 * Draw Attack's images.
	 *
	 * @param g
	 *            the Graphic Manager.
	 */
	private void drawAttackImage(Deque<LoopEffect> projectiles, Graphics2D screenGraphic) {

		// Is displayed according to the orientation image attack.
		for (LoopEffect projectile : projectiles) {
			Attack attack = projectile.getAttack();
			Image image = projectile.getImage();
			HitArea area = attack.getCurrentHitArea();

			int positionX;
			if (attack.getSpeedX() >= 0) {
				positionX = area.getRight() - (image.getWidth() * 5 / 6);
			} else {
				positionX = area.getLeft() - (image.getWidth() * 1 / 6);
			}

			if (attack.getCurrentFrame() > attack.getStartUp()) {
				BufferedImage tmpImage = image.getBufferedImage();
				flipImage(tmpImage, attack.isPlayerNumber());

				int positionY = area.getTop() - ((image.getHeight() - (area.getBottom() - area.getTop())) / 2);
				screenGraphic.drawImage(tmpImage, positionX, positionY, image.getWidth(), image.getHeight(), null);

				GraphicManager.getInstance().drawImage(image, positionX, positionY, image.getWidth(), image.getHeight(),
						attack.isPlayerNumber());
			}
		}
	}

	/**
	 *
	 * Draws both characters' HP information.
	 */
	private void drawHPGaugeImage(Character[] playerCharacters) {
		if (FlagSetting.limitHpFlag) {
			int p1Hp = (int) ((double) playerCharacters[0].getHp() / LaunchSetting.maxHp[0] * 300 * -1);
			int p2Hp = (int) ((double) playerCharacters[1].getHp() / LaunchSetting.maxHp[1] * 300);

			GraphicManager.getInstance().drawQuad(480 - 50, 75, -300, 20, 0.2f, 0.2f, 0.2f, 0.0f);
			GraphicManager.getInstance().drawQuad(480 + 50, 75, 300, 20, 0.2f, 0.2f, 0.2f, 0.0f);
			GraphicManager.getInstance().drawQuad(480 - 50, 75, p1Hp, 20, 0, 1.0f, 0, 0.0f);
			GraphicManager.getInstance().drawQuad(480 + 50, 75, p2Hp, 20, 1.0f, 0.65f, 0, 0.0f);

			GraphicManager.getInstance().drawString("P1 HP:" + playerCharacters[0].getHp(), 130 + 30, 50);
			GraphicManager.getInstance().drawString("P2 HP:" + playerCharacters[1].getHp(), 590 - 30, 50);

		} else {
			GraphicManager.getInstance().drawString("P1 HP:" + playerCharacters[0].getHp(), 100, 50);
			GraphicManager.getInstance().drawString("P2 HP:" + playerCharacters[1].getHp(), 760, 50);
		}
	}

	/**
	 *
	 * Draws both characters' energy information.
	 */
	private void drawEnergyGaugeImage(Character[] playerCharacters) {
		if (FlagSetting.limitHpFlag) {
			float[] red = { 1.0f, 1.0f };
			float[] green = { 0.0f, 0.0f };
			float[] blue = { 0.0f, 0.0f };

			for (int i = 0; i < 2; i++) {
				int energy = playerCharacters[i].getEnergy();

				if (energy >= 50 && energy < LaunchSetting.maxEnergy[i]) {
					red[i] = 1.0f;
					green[i] = 1.0f;
					blue[i] = 0.0f;
				} else if (energy >= LaunchSetting.maxEnergy[i]) {
					red[i] = 0.0f;
					green[i] = 0.0f;
					blue[i] = 1.0f;
				}
			}

			int p1Energy = (int) ((float) playerCharacters[0].getEnergy() / LaunchSetting.maxEnergy[0] * 300 * -1);
			int p2Energy = (int) ((float) playerCharacters[1].getEnergy() / LaunchSetting.maxEnergy[1] * 300);

			GraphicManager.getInstance().drawQuad(480 - 50, 75 + 20, p1Energy, 8, red[0], green[0], blue[0], 0.0f);
			GraphicManager.getInstance().drawQuad(480 + 50, 75 + 20, p2Energy, 8, red[1], green[1], blue[1], 0.0f);
			GraphicManager.getInstance().drawString("ENERGY:" + playerCharacters[0].getEnergy(), 250 + 30, 50);
			GraphicManager.getInstance().drawString("ENERGY:" + playerCharacters[1].getEnergy(), 710 - 30, 50);

		} else {
			GraphicManager.getInstance().drawString("P1 ENERGY:" + playerCharacters[0].getEnergy(), 100, 100);
			GraphicManager.getInstance().drawString("P2 ENERGY:" + playerCharacters[1].getEnergy(), 760, 100);
		}
	}

	/**
	 *
	 * Draws time.
	 *
	 * @param remainingTime
	 *            the remaining time.
	 */
	private void drawTimeImage(int remainingTime) {
		GraphicManager.getInstance().drawString(Integer.toString(remainingTime), GameSetting.STAGE_WIDTH / 2 - 30, 10);
	}

	/**
	 *
	 * Draws round number.
	 *
	 */
	private void drawRoundNumber(int round) {
		GraphicManager.getInstance().drawString("ROUND:" + (round + 1), 850, 10);
	}

	/**
	 *
	 * Draws the combo hit counter.
	 *
	 * @param g
	 *            the Graphic Manager
	 */
	private void drawHitCounter(Character[] playerCharacters) {
		for (int i = 0; i < 2; ++i) {
			int comboState = playerCharacters[i].getComboState();

			if (comboState > 0) {
				Image counterImage = GraphicManager.getInstance().getCounterTextImageContainer().get(comboState - 1);
				GraphicManager.getInstance().drawImage(counterImage, i == 0 ? 100 : 760, 150, Image.DIRECTION_RIGHT);

				Image hitTextImage = GraphicManager.getInstance().getHitTextImageContainer().get(0);
				GraphicManager.getInstance().drawImage(hitTextImage, i == 0 ? 170 : 830, 150, Image.DIRECTION_RIGHT);

			}
		}
	}

	/**
	 * Draws character's hit area
	 */
	private void drawHitArea(Character[] playerCharacters, Deque<LoopEffect> projectiles) {
		for (int i = 0; i < 2; ++i) {

			// キャラクターの当たり判定ボックスの描画
			// P1とP2で色を変える
			GraphicManager.getInstance().drawLineQuad(playerCharacters[i].getHitAreaLeft(),
					playerCharacters[i].getHitAreaTop(),
					playerCharacters[i].getHitAreaRight() - playerCharacters[i].getHitAreaLeft(),
					playerCharacters[i].getHitAreaBottom() - playerCharacters[i].getHitAreaTop(), 0.0f + i,
					1.0f - i * 0.35f, 0.0f, 0.0f);

			// 攻撃の当たり判定ボックスの描画
			if (playerCharacters[i].getAttack() != null) {
				HitArea area = playerCharacters[i].getAttack().getCurrentHitArea();

				GraphicManager.getInstance().drawLineQuad(area.getLeft(), area.getTop(),
						area.getRight() - area.getLeft(), area.getBottom() - area.getTop(), 1.0f, 0.0f, 0.0f, 0.0f);
			}
		}

		// 波動拳の当たり判定ボックスの描画
		for (LoopEffect loopEffect : projectiles) {
			Attack temp = loopEffect.getAttack();

			if (temp.getCurrentFrame() > temp.getStartUp()) {
				HitArea area = temp.getCurrentHitArea();

				GraphicManager.getInstance().drawLineQuad(area.getLeft(), area.getTop(),
						area.getRight() - area.getLeft(), area.getBottom() - area.getTop(), 1.0f, 0.0f, 0.0f, 0.0f);
			}
		}
	}

	private void drawHitEffects(LinkedList<LinkedList<HitEffect>> hitEffects, Graphics2D screenGraphic) {

		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < hitEffects.get(i).size(); ++j) {
				HitEffect hitEffect = hitEffects.get(i).get(j);

				if (hitEffect.isHit()) {
					HitArea area = hitEffect.getAttack().getCurrentHitArea();
					Image image = hitEffect.getImage();

					BufferedImage tmpImage = image.getBufferedImage();
					flipImage(tmpImage, i != 0);

					int positionX = area.getLeft() - (image.getWidth() - area.getRight() + area.getLeft()) / 2
							+ hitEffect.getXVariation();
					int positionY = area.getTop() - (image.getHeight() - area.getBottom() + area.getTop()) / 2
							+ hitEffect.getYVariation();
					screenGraphic.drawImage(tmpImage, positionX, positionY, image.getWidth(), image.getHeight(), null);

					GraphicManager.getInstance().drawImage(image, positionX, positionY, image.getWidth(),
							image.getHeight(), i == 0 ? Image.DIRECTION_RIGHT : Image.DIRECTION_LEFT);
				}
			}
		}
	}

	private BufferedImage flipImage(BufferedImage image, boolean isFront) {
		// Flip the image if we need to
		if (!isFront) {
			AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-image.getWidth(null), 0);

			AffineTransformOp flip = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			image = flip.filter(image, null);
		}

		return image;
	}

}
