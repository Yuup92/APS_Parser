
public class VI extends Solver {
	
	private double[][] qTable;
	private double[][] qTablePrev;
	
	public VI(POMDP mdp) {
		super(mdp);
		initializeQTable();
	}
	
	public void Solve() {
		// Made assert that discount factor has to be between 0-1
		int sNext,s,a;
		double sum;
		double delta = 1;
		int count = 0;
		while(delta > 0.01) {
			delta = 0;
			count++;
			for(s = 0; s < this.mdp.getNumStates(); s++) {
				for(a = 0; a < this.mdp.getNumActions(); a++) {
					sum = 0.0;
					
					for(sNext = 0; sNext < this.mdp.getNumStates(); sNext++) {
						sum = sum + this.mdp.getTransitionProbability(s, a, sNext)*getMaxQTablePrev(sNext);
					}
					this.qTable[s][a] = this.mdp.getReward(s, a) + this.mdp.getDiscountFactor()*sum;
					delta = getDelta(delta, s, a);
					//System.out.format("Delta: %f%n", delta);
				}
			}
			saveCurrentQMatrix();
		}
		printQTable();
		System.out.format("The amount of cycles was: %d%n", count);
	}
	
	private double getMaxQTablePrev(int s) {
		double max = 0;

		for(int i = 0; i < this.mdp.getNumActions(); i++) {
			if(max < qTablePrev[s][i]) {
				max = qTablePrev[s][i];
			}
		}
		return max;
	}
	
	private void printQTable() {
		int s,a;
		
		for( s=0; s<this.mdp.getNumStates(); s++) {
			for( a=0; a<this.mdp.getNumActions(); a++) {
				System.out.format("%06.3f  ", qTable[s][a]);
			}
			System.out.format("%n");
		}
		System.out.format("%n%n%n");
		
	}
	
	private void saveCurrentQMatrix() {
		for(int s = 0; s < this.mdp.getNumStates(); s++) {
			for(int a = 0; a < this.mdp.getNumActions(); a++) {
				this.qTablePrev[s][a] = this.qTable[s][a];
			}
		}
	}
	
	private double getDelta(double delta, int s, int a) {
		double d;	
		d = Math.abs(this.qTablePrev[s][a] - this.qTable[s][a]);
		if(delta < d) {
			return d;
		}
		return delta;
	}
	
	private void initializeQTable() {
		this.qTable = new double[this.mdp.getNumStates()][this.mdp.getNumActions()];
		this.qTablePrev = new double[this.mdp.getNumStates()][this.mdp.getNumActions()];
	}
}
