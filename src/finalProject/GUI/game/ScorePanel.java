package finalProject.GUI.game;

import java.awt.Color;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class ScorePanel extends JPanel{
	private int nScore;
	private JLabel uScoreLabel;
	
	public ScorePanel(JPanel uPanel){
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBounds(0, 0, 100, 20);
		this.setBorder(new LineBorder(Color.BLACK));
		this.uScoreLabel = new JLabel("Score: 0");
		this.add(uScoreLabel);
		uPanel.add(this);
	}
	
	public void reset(){
		this.nScore = 0;
		this.uScoreLabel.setText("Score: " + this.nScore);
	}
	
	public void add(int nAdd){
		this.nScore += nAdd;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Score: " + this.nScore);
	}
	
	public void subtract(int nSub){
		this.nScore -= nSub;
		if(this.nScore < 0){
			this.nScore = 0;
		}
		this.uScoreLabel.setText("Score: " + this.nScore);
	}
	
}
