package com.dist.wx.msg.resp;

/**
 * ������Ϣ
 * 
 * @author Engineer.Jsp
 * @date 2014.10.08*
 */
public class VoiceMessage extends BaseMessage {
	// ����
	private Voice Voice;

	public Voice getVoice() {
		return Voice;
	}

	public void setVoice(Voice voice) {
		Voice = voice;
	}
}
