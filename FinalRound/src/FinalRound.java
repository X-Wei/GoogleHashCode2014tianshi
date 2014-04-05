import java.util.Random;

public class FinalRound {
	static int N = 11348,// nb junctions
			M = 17958,// nb street
			T = 54000,// total time
			C = 8,// nb cars
			S = 4516;// index start junction

	static Graph graph = new Graph();
	static Junction[] juncs = graph.juncs;
	static Street[] streets = graph.streets;
	private static Random rnd = new Random(System.currentTimeMillis());


	public static void main(String[] args) {
		
		int length=0;
		boolean flag=true;
//		while(flag){
		for(int r=10;r<100;r+=5){
			
			length=run1by1(50,r);
			System.out.println("r= "+r+ "; length="+length);
			if(length>1562525){
				flag=false;
				return;
			}
			length=runPara(50,r);
			System.out.println("r= "+r+ "; length="+length);
			if(length>1562525){
				flag=false;
				return;
			}
		}
		
		System.out.println("over! length= " + length);
	}// main

	
	
	
	
	
	
	
	
	
	static int runPara(int ratio,int ctr1){
		TC.lectureDansFichier("paris_54000.txt");
		TC.ecritureDansNouveauFichier("out.txt");
		TC.println(8);
		graph = new Graph();
		juncs = graph.juncs;
		streets = graph.streets;
		rnd=new Random(System.currentTimeMillis());
		readData();
		int length = 0;// total length
		int i;
		int[] positionsArr = new int[C];
		int[] timeLeftArr = new int[C];
		int[] nbptsArr = new int[C];
		boolean[] stopped = new boolean[C];
		int nbRunning = C;
		String[] cmds = new String[C];
		for (i = 0; i < C; i++) {
			cmds[i] += S + "\n";
			nbptsArr[i] = 1;
			positionsArr[i] = S;
			timeLeftArr[i] = T;
			stopped[i] = false;
		}// end init

		while (nbRunning > 0) {
			for (i = 0; i < C; i++) {
				if (stopped[i]==false) {// if car-i running
					int j = positionsArr[i];
					juncs[positionsArr[i]].visitedTime++;
					int[] nextStep = findNext(j, timeLeftArr[i], ratio,i>6,nbptsArr[i],ctr1,i);// make
																	// decision

					positionsArr[i] = nextStep[0];// go to the next junc
					if (positionsArr[i] == -1) {
						nbRunning--;
						stopped[i] = true;
//						System.out.println(nbRunning);
//						System.out.println("time for car "+i+": "+timeLeftArr[i]);
					} else {
						
						int nextst = nextStep[2];// index of next street
						streets[nextst].visited++;// mark as visited!!
						timeLeftArr[i] -= streets[nextst].cost;
						length += nextStep[1];

						cmds[i] += (positionsArr[i] + "\n");
						nbptsArr[i]++;
					}
					

				}
			}// for
		}// end while
		for(i=0;i<C;i++){
			TC.println(nbptsArr[i]);
			TC.print(cmds[i]);
		}
		return length;
}
	
	
	static int run1by1(int ratio,int ctr1){
		TC.lectureDansFichier("paris_54000.txt");
		TC.ecritureDansNouveauFichier("out.txt");
		TC.println(8);
		graph = new Graph();
		juncs = graph.juncs;
		streets = graph.streets;
		rnd=new Random(System.currentTimeMillis());
		readData();
		int i=0;
		int length = 0;// total length
		for (i = 0; i < C; i++) {

			int j = S, timeLeft = T;// car-i
			int leni=0;
			String cmd = (S + "\n");// for car-i give cmds
			int nbpts = 1;// nb of juncs visited by car i

			// car-i go!!
			while (timeLeft > 0) {
				
				int[] nextStep = findNext(j, timeLeft, ratio,i>6,nbpts,ctr1,i);// make decision
				
				juncs[j].visitedTime++;
				j = nextStep[0];// go to the next junc
				if (j == -1)
					break;

				int nextst = nextStep[2];// index of next street
				streets[nextst].visited++;// mark as visited!!
				timeLeft -= streets[nextst].cost;
				length += nextStep[1];
				leni+=nextStep[1];

				cmd += (j + "\n");
				nbpts++;
			}
//			System.out.println(i + " " + timeLeft);
//			System.out.println(i + " length: " + leni);
			TC.println(nbpts);
			TC.print(cmd);
		}// end for car-i
		return length;
	}//run1by1
	
	
	static void readData() {
		// read input to vars
		juncs = new Junction[N];
		streets = new Street[M];
		TC.lireLigne();
		int i;
		for (i = 0; i < N; i++) {// N rows: coords of the junctions: rubbish
			String[] strs=TC.motsDeChaine(TC.lireLigne());
			float x=Float.parseFloat(strs[0]);
			float y=Float.parseFloat(strs[1]);
			juncs[i] = new Junction(i,x,y);
		}

		for (i = 0; i < M; i++) {
			// for street-i
			String[] s = TC.motsDeChaine(TC.lireLigne());
			int from = Integer.parseInt(s[0]);
			int to = Integer.parseInt(s[1]);
			boolean bidir = Integer.parseInt(s[2]) == 2;
			int cost = Integer.parseInt(s[3]);
			int len = Integer.parseInt(s[4]);
			Street st = new Street(from, to, bidir, cost, len);
			streets[i] = st;

			// update voisin info
			juncs[from].voisinList.add(to);
			if (bidir) {
				juncs[to].voisinList.add(from);
				juncs[to].stList.add(i);
			}
			juncs[from].stList.add(i);
		}
		System.out.println("data read!");
	}// readData()

	static int[] findNext(int junc, int timeleft, int ratio, boolean longsighted, int counter1,int counter2, int dir) {
		// return 3 numbers: [nextJunc, len, nextSt]
		int len = 0, nextJunc = -1, nextSt = -1;
		float eff = 0;
		int lowestCost = 100000, lowestCostJunc = -1, lowestCostSt = -1;
		int lowestVisitedTime = 100000, lowestVisitedJunc = -1, lowestVisitedSt = -1;
		int stLeastVisitedTime = 100000, stLeastVisitedStJunc = -1, stLeastVisitedSt = -1;
		int maxSumLen = 0, maxSumLenJunc = -1, maxSumLenSt = -1;
		int maxProsp = 0, maxProspJunc = -1, maxProspSt = -1;
		float dirmax=-9999999;int dirmaxJunc=-1,dirmaxSt=-1;
		for (int st : juncs[junc].stList) {// st
			Street street = streets[st];
			int otherPt = (street.from == junc ? street.to : street.from);// 另一头

			//find dirmax
			if(juncs[otherPt].dirModule(dir)>dirmax){
				dirmax=juncs[otherPt].dirModule(dir);
				dirmaxJunc=otherPt;
				dirmaxSt=st;
			}
			
			// find max length ==> nextJunc
			if (street.visited == 0 && timeleft >= street.cost
					&& street.len >= len) {// street.effeciency() > eff eff =
											// street.effeciency();
				if(street.len == len )
					len = rnd.nextBoolean()?len:street.len;
				else{
					len = street.len;
					nextJunc = otherPt;
					nextSt = st;
				}
			}
			// find maxSumLen
			if (juncs[otherPt].sumLen() > maxSumLen) {
				// street.visited == 0 && timeleft >= street.cost &&
				maxSumLen = juncs[otherPt].sumLen();
				maxSumLenJunc = otherPt;
				maxSumLenSt = st;
			}
			// find lowest cost street
			if (street.cost < lowestCost) {
				lowestCost = street.cost;
				lowestCostJunc = otherPt;
				lowestCostSt = st;
			}
			// find least visited voisin
			if (juncs[otherPt].visitedTime < lowestVisitedTime) {
				lowestVisitedTime = juncs[otherPt].visitedTime;
				lowestVisitedJunc = otherPt;
				lowestVisitedSt = st;
			}
			// find least visited st
			if (street.visited < stLeastVisitedTime) {
				stLeastVisitedTime = street.visited;
				stLeastVisitedStJunc = otherPt;
				stLeastVisitedSt = st;
			}
			//find max prosp
			if(timeleft >= street.cost &&
					juncs[otherPt].prosp(junc)>maxProsp ){
				
				maxProsp=juncs[otherPt].prosp(junc);
				maxProspJunc=otherPt;
				maxProspSt=st;
				
//				len = street.visited == 0?street.len:0;//delete......
//				nextJunc = otherPt;
//				nextSt = st;
			}
		}// end going over juncs[junc].stList
		
		
		
		if(counter1>=counter2 && counter1<counter2+ratio){//para
			nextJunc=dirmaxJunc;
			nextSt=dirmaxSt;
			len=(streets[dirmaxSt].visited == 0?streets[nextSt].len:0);
			int[] res = { nextJunc, len, nextSt };
			return res;
		}

		if(false &&longsighted && len>0 && maxProsp>ratio*len){
			nextJunc=maxProspJunc;
			nextSt=maxProspSt;
			len=(streets[nextSt].visited == 0?streets[nextSt].len:0);
			int[] res = { nextJunc, len, nextSt };
			return res;
		}
		
		if (nextJunc == -1) {// if we cannot increase length ...
			//try maxprosp first
//			if(maxProsp>0 && streets[maxProspSt].cost<=timeleft){
//				nextJunc=maxProspJunc;
//				nextSt=maxProspSt;
//				len=(streets[nextSt].visited == 0?streets[nextSt].len:0);
//				int[] res = { nextJunc, len, nextSt };
//				return res;
//			}
			
			// try stLeastVisitedSt first
			// if(streets[stLeastVisitedSt].cost<=timeleft){//if we can go to
			// stLeastVisitedSt
			// len=0;
			// nextJunc = stLeastVisitedStJunc;
			// nextSt = stLeastVisitedSt;
			// int[] res = { nextJunc, len, nextSt };
			// return res;
			// }

			// try maxSumLen first
			// if(maxSumLen>0 && streets[maxSumLenSt].cost <= timeleft){//if we
			// can go to maxSumLenSt
			// len = 0;
			// nextJunc = maxSumLenJunc;
			// nextSt = maxSumLenSt;
			// int[] res = { nextJunc, len, nextSt };
			// return res;
			// }
			// //if we cannot go to maxSumLenSt
			// // ==>try the less visited points!
			 //else
			if (streets[lowestVisitedSt].cost <= timeleft) {
				len = 0;
				nextJunc = lowestVisitedJunc;
				nextSt = lowestVisitedSt;
				int[] res = { nextJunc, len, nextSt };
				return res;
			}

			else {// if we cannot even go to least visited pt==> try
					// lowestcostSt
				if (lowestCost <= timeleft) {// if we can go lowestcost road?...
					len = 0;
					nextJunc = lowestCostJunc;
					nextSt = lowestCostSt;
					int[] res = { nextJunc, len, nextSt };
					return res;
				} else {// we cannot go anywhere
//					System.out.println("bbbbbbbbbbb!");
					len = 0;
					nextJunc = -1;
					nextSt = -1;
					int[] res = { nextJunc, len, nextSt };
					return res;
				}
			}// end if lowestVisited cost>timeleft

		}

		int[] res = { nextJunc, len, nextSt };
		return res;
	}// findNext
	
	
}// class
