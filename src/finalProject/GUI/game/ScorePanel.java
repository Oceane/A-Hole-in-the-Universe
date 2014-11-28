package finalProject.GUI.game;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ScorePanel extends JPanel{
	public static final Color uSemiTrans = new Color(0, 0, 0, 150);
	private int nScore;
	private JLabel uScoreLabel;
	
	public ScorePanel(JPanel uPanel){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBounds(0, 0, 100, 20);
		this.setBackground(uSemiTrans);
		this.uScoreLabel = new JLabel("Damage: 0");
		this.uScoreLabel.setForeground(Color.white);
		this.add(uScoreLabel);
		uPanel.add(this);
	}
	
	public void reset(){
		this.nScore = 0;
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}
	
	public void add(int nAdd){
		this.nScore += nAdd;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}
	
	public void subtract(int nSub){
		this.nScore -= nSub;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Damage: " + this.nScore);
	}
	
}
