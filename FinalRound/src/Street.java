public class Street {

	int from, to;
	int cost;
	int len;
	boolean bidir;
	int visited = 0;

	Street(int f, int t, boolean bd, int c, int l) {
		from = f;
		to = t;
		cost = c;
		len = l;
		bidir = bd;
		visited = 0;
	}

	float effeciency() {
		return ((float)len) / ((float)cost);
	}
}
