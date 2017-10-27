package manager;

import static org.lwjgl.glfw.GLFW.*;

import aiinterface.AIController;
import input.KeyData;
import input.Keyboard;
import struct.Key;

/** AIやキーボード等の入力関連のタスクを管理するマネージャー */
public class InputManager<Data> {
	private static  InputManager inputManager = new  InputManager();

	private KeyData buffer;

	/*KeyCallBackクラス*/
	private Keyboard keyboard;

	private AIController[] ais;

	// static field
	/** Default number of devices **/
	private final static int DEFAULT_DEVICE_NUMBER = 2;

	/** Default device type is keyboard **/
	public final static char DEVICE_TYPE_KEYBOARD = 0;
	public final static char DEVICE_TYPE_CONTROLLER = 1;
	public final static char DEVICE_TYPE_AI = 2;

	// to recognize the input devices
	private char[] deviceTypes;

	private  InputManager() {
		System.out.println("Create instance: " + InputManager.class.getName());
		keyboard = new Keyboard();
		deviceTypes = new char[DEFAULT_DEVICE_NUMBER];
		for(int i = 0; i < this.deviceTypes.length ; i++){
			this.deviceTypes[i] = DEVICE_TYPE_KEYBOARD;
		}
	}

	public static  InputManager getInstance() {
		return  inputManager;
	}

	public Keyboard getKeyboard(){
		return this.keyboard;
	}

	public void update(){
		//DesplayManager内にある
		//glfwPollEvents();

		Key[] keys = new Key[this.deviceTypes.length];
		for(int i = 0; i < this.deviceTypes.length; i++  ){
			switch (this.deviceTypes[i]){
				case DEVICE_TYPE_KEYBOARD:
					keys[i] = getKeyFromKeyboard(i);
					break;
				case DEVICE_TYPE_AI:
//					keys[i] = getKeyFromAI();
					break;
				default:
					break;
			}
		}


//		keys[0] = getKeyFromKeyboard(true);
//		keys[1] = getKeyFromKeyboard(false);

		this.setKeyData(new KeyData(keys));
	}

	private Key getKeyFromKeyboard(int player){
		Key key = new Key();
		key.A = keyboard.getKeyDown(GLFW_KEY_Z);
		key.B = keyboard.getKeyDown(GLFW_KEY_X);
		key.C = keyboard.getKeyDown(GLFW_KEY_C);
		key.U = keyboard.getKeyDown(GLFW_KEY_UP);
		key.D = keyboard.getKeyDown(GLFW_KEY_DOWN);
		key.R = keyboard.getKeyDown(GLFW_KEY_RIGHT);
		key.L = keyboard.getKeyDown(GLFW_KEY_LEFT);

		return key;
	}

//	private synchronized Key getInputFromAI(AIController ai){
	private Key getInputFromAI(AIController ai){
		if(ai == null)
			return new Key();
		return new Key(ai.getInput());
	}

	public KeyData getKeyData() {
		return this.buffer;
	}

	public void setKeyData(KeyData data) {
		this.buffer = data;
	}

}
