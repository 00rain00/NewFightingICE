rem bat�t�@�C���T���v��
setlocal ENABLEDELAYEDEXPANSION

rem ���s������AI�̐�-1
set FIGHT_AI_NUM=1

rem ���s������AI�̖��O
set FIGHT_AI[0]=RandomCommandAI
set FIGHT_AI[1]=RandomActionAI

rem �g�p�L�����N�^�[
set CHARACTER=ZEN

rem �S�g�ݍ��킹�ő�������
for /l %%i in (0,1,!FIGHT_AI_NUM!) do (
	for /l %%j in (0,1,!FIGHT_AI_NUM!) do (
		java -cp FightingICE.jar;./lib/lwjgl/*;./lib/natives/windows/*;./lib/*;  Main --a1 !FIGHT_AI[%%i]! --a2 !FIGHT_AI[%%j]! --c1 !CHARACTER! --c2 !CHARACTER! -n 1
		java -cp FightingICE.jar;./lib/lwjgl/*;./lib/natives/windows/*;./lib/*;  Main --a1 !FIGHT_AI[%%j]! --a2 !FIGHT_AI[%%i]! --c1 !CHARACTER! --c2 !CHARACTER! -n 1
	)
)

rem TIMEOUT /T -1
endlocal
exit
