package com.Tsp;

public class TspData {
	int citysNum;
	float[][] disMap;
	{ 
		int[][] citys={
        {1304,        2312},{3639,        1315},         
        {4177,        2244},{3712,        1399},            
        {3488,        1535},{3326,        1556},         
        {3238,        1229},{4196,        1004},         
        {4312,         790},{4386,         570},
        {3007,        1970},{2562,        1756},
        {2788,        1491},{2381,        1676},
        {1332,         695},{3715,        1678},
        {3918,        2179},{4061,        2370},
        {3780,        2212},{3676,        2578},
        {4029,        2838},{4263,        2931},
        {3429,        1908},{3507,        2367},
        {3394,        2643},{3439,        3201},
        {2935,        3240},{3140,        3550},
        {2545,        2357},{2778,        2826},
        {2370,        2975}};//31个城市（最优解:14700）
	
		citysNum = citys.length;
		disMap = new float[citysNum][citysNum];	
		for(int i = 0;i<citysNum;i++) {
			for(int j=0;j<i;j++) {
				if(i!=j) {
					float dis = (float)Math.sqrt(Math.pow((citys[i][0]-citys[j][0]), 2)+Math.pow((citys[i][1]-citys[j][1]), 2));
					disMap[i][j] = dis;
					disMap[j][i] = dis;
				}else {
					disMap[i][j] = 0.0f;
				}
			}
		}
	}

}
