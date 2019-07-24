package com.Tsp;
import java.util.Random;
import java.util.Arrays;
import com.myfun.mymath;
public class Ga{
	final int popsize = 300;		// 种群大小
	final int maxGen = 600;			// 最大迭代次数
	final float pc = 0.9f;			// 交叉概率
	final float pm = 0.2f;			// 变异概率
	final float gapp = 0.8f;		// 选择因子
	int citysNum;					// 城市个数
	float[][] disMap;				// 路网
	int[][] pop;					// 种群
	float[] values = new float[popsize];		// 种群目标函数值（这里指路线距离）
	mymath mathFun = new mymath();				
	Random rand = new Random();
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
		pop = new int[popsize][citysNum];
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
	public int[] createInd(int citysNum) {
		// 随机产生个体


		int[] ind = new int[citysNum];
		for(int k = 0;k<citysNum;k++) {
			ind[k] = k;
		}
		for(int i=0;i<citysNum;i++) {
			int num = i+rand.nextInt(citysNum-i);
			int tmp;
			tmp = ind[num];
			ind[num] = ind[i];
			ind[i] = tmp;
		}
		return ind;
	}
	
	public int[][] createPop(){
		// 创建种群
		for(int i=0;i<popsize;i++) {
			pop[i] = createInd(citysNum).clone();
		}
		return pop;
	}
	
	public float calDistance(int[] x) {
		// 解码计算个体目标函数值
		float dis = 0.0f;
		for(int i=0;i<citysNum-1;i++) {
			dis += disMap[x[i]][x[i+1]];
		}
		dis += disMap[x[0]][x[citysNum-1]];
		return dis;
	}
	
	public float[] decodePop(int[][] pop) {
		// 解码计算种群目标函数值
		for(int i=0;i<popsize;i++) {
			values[i] = calDistance(pop[i].clone());
		}
		return values;
	}
	public float[] fitness(float[] val) {
		// 适应度分配
		float valSum = mathFun.sumfloat(val);
		float[] fits = new float[val.length];
		for(int i=0;i<val.length;i++) {
			fits[i] = val[i]/valSum;
		}
		return fits;
	}
	
	public int[][] select(int[][] pop,float[] fits){
		// 选择操作
		int sonNum = (int)(gapp*pop.length);
		int[][] chrome = new int[sonNum][citysNum];
		float[] indRate = mathFun.comsumFloat(fits);
		for(int i=0;i<sonNum;i++) {
			float rate = rand.nextFloat();
			for(int j=0;j<pop.length;j++) {
				if (rate <= indRate[j]) {
					chrome[i] = pop[j].clone();
					break;
				}
			}
		}
		
		return chrome;
	}
	
	public int[][] crossover(int[][] pop){
		// 交叉操作
		int[] index = createInd(pop.length);
		for(int i=0;i<pop.length-1;i+=2) {
			float rate = rand.nextFloat();
			if (rate < pc) {
				int[] ind1 = pop[index[i]].clone();
				int[] ind2 = pop[index[i+1]].clone();
//				System.out.println(Arrays.toString(ind1));
//				System.out.println(Arrays.toString(ind2));
				int r1 = rand.nextInt(citysNum);
				int r2 = rand.nextInt(citysNum);
				while (r1>r2) {
					int temp = r1;
					r1 = r2;
					r2 = temp;
				}
				for(int j=r1;j<r2;j++) {
					int[] a = ind1.clone();
					int[] b = ind2.clone();
					int gen1 = ind1[j];
					int gen2 = ind2[j];
					ind2[j] = gen1;
					ind1[j] = gen2;
					for (int k=0;k<citysNum;k++) {
						if(a[k]==gen2&& k!=j) {
							ind1[k] = gen1;
							break;
						}
					}
					for (int k=0;k<citysNum;k++) {
						if(b[k]==gen1&& k!=j) {
							ind2[k] = gen2;
							break;
						}
					}
					
				}
//				System.out.println(Arrays.toString(ind1));
//				System.out.println(Arrays.toString(ind2));
				pop[index[i]] = ind1.clone();
				pop[index[i+1]] = ind2.clone();
			}
		}
		return pop;
	}
	
	public int[][] mutate(int[][] pop){
		// 变异操作
		for(int i = 0;i<pop.length;i++) {
			float rate = rand.nextFloat();
			if (rate < pm){
				int[] ind3 = pop[i].clone();
//				System.out.println(Arrays.toString(ind3));
				int r1 = rand.nextInt(citysNum);
				int r2 = rand.nextInt(citysNum);
				while (r1>r2) {
					int temp = r1;
					r1 = r2;
					r2 = temp;
				}
				while(r1<r2) {
					int temp;
					temp = ind3[r1];
					ind3[r1] = ind3[r2];
					ind3[r2] = temp;
					r1++;
					r2--;
				}
//				System.out.println(Arrays.toString(ind3));
				pop[i] = ind3.clone();
			}
		}
		return pop;
	}
	public int[][] reins(int[][] chrome,int[][] pop,float[] values){
		// 基因重组
		int[][] sonPop = new int[popsize][citysNum];
		int[] index = mathFun.indexSort(values);
		for(int i=0;i<popsize;i++) {
			if(i<chrome.length) {
				sonPop[i] = chrome[i].clone();
			}else {
				sonPop[i] = pop[index[i-chrome.length]].clone();
			}
		}
		return sonPop;
	}
	
	public int getBest(float[] values) {
		// 寻找最优个体下标
		int[] index = mathFun.indexSort(values);
		int bestIndex = index[0];
		return bestIndex;
	}
	
	public static void main(String[] args) {
		Ga GaTsp = new Ga();
		int[][] pop = GaTsp.createPop();
		float[] values = GaTsp.decodePop(pop);
		float[] valuesCopy = values.clone();
		int bestIndex = GaTsp.getBest(valuesCopy);
		float bestDis = values[bestIndex];
		int[] bestInd = pop[bestIndex].clone();
		float[] bestResult = new float[GaTsp.maxGen];
		for(int gen=0;gen<GaTsp.maxGen;gen++) {
			float[] fits = GaTsp.fitness(values.clone());
			int[][] chrome = GaTsp.select(pop, fits);
			chrome = GaTsp.crossover(chrome);
			chrome = GaTsp.mutate(chrome);
			pop = GaTsp.reins(chrome, pop, values.clone());
			values = GaTsp.decodePop(pop);
			valuesCopy = values.clone();
			bestIndex =  GaTsp.getBest( valuesCopy);
			float dis = values[bestIndex];
			if (dis < bestDis) {
				bestDis = dis;
				bestInd = pop[bestIndex].clone();
			}
			bestResult[gen] = bestDis;
		}
		System.out.println(bestDis);
		System.out.println(Arrays.toString(bestInd));
	
//		System.out.println(Arrays.toString(bestResult));
		
	}
	
}
