
public class Main {
	public static void main(String[] args) {
		//POMDP pomdp = ParsePOMDP.readPOMDP("domains/hallway2.POMDP");
		//POMDP pomdp = ParsePOMDP.readPOMDP("domains/tiger.aaai.POMDP");
		POMDP pomdp = ParsePOMDP.readPOMDP("domains/saci-s100-a10-z31.POMDP");
		VI vi = new VI(pomdp);
		vi.Solve();
		// pomdp.getTransitionProbability(s, a, sNext);
		// pomdp.getReward(s, a)
		// pomdp.getNumStates()
	}
}
