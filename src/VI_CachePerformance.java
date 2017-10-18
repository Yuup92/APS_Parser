public class VI_CachePerformance extends Solver {
	
	private double[][] qTable;
	private double[][] qTablePrev;
	private double[]   vt;
	
	public VI_CachePerformance(POMDP mdp) {
		super(mdp);
		initializeQTable();
		initializeVt();
	}
	
	public void Solve() {
		// Make assert that discount factor has to be between 0-1
		int sNext,s,a;
		double sum;
		double delta = 1;
		int count = 0;
		while(delta > 0.01) {
			delta = 0;
			count++;
			//Iterating over each State
			for(s = 0; s < this.mdp.getNumStates(); s++) {
				double bellman;
				//Iterating over each Action
				for(a = 0; a < this.mdp.getNumActions(); a++) {
					sum = 0.0;
					for(sNext = 0; sNext < this.mdp.getNumStates(); sNext++) {
						sum = sum + this.mdp.getTransitionProbability(s, a, sNext)*getMaxQTablePrev(sNext);
					}
					bellman = this.mdp.getReward(s, a) + this.mdp.getDiscountFactor()*sum;
					this.qTable[s][a] = bellman;
					
					//Find the highest reward action
					if(vt[s] < bellman) {
						vt[s] = bellman;
					}
					delta = getDelta(delta, s, a);
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
	
	private void initializeVt() {
		this.vt = new double[this.mdp.getNumStates()];
	}
}
