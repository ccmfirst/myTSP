package com.Tsp;

import java.util.Arrays;
import java.util.Random;

import com.myfun.mymath;

public class ACO extends TspData {
	
	final int popsize = 50;		  	// 种群大小
	final int maxGen = 200;			// 最大迭代次数
	final float alpha = 1.0f;		// 信息素重要程度因子
	final float beta = 5.0f;		// 启发函数重要程度因子
	final float rho = 0.1f;			// 信息素挥发因子
	final float q = 1.0f;			// 常系数
	float[][] eta = new float[citysNum][citysNum];					// 启发函数
	float[][] Tau = new float[citysNum][citysNum];					// 信息素矩阵
	int[][] Table = new int[popsize][citysNum];						// 路径记录表
	int[][] routeBest = new int[maxGen][citysNum];					// 各代最佳路径
	float[] lengthBest = new float[maxGen];		// 各代最佳路径的长度
	float[] lengthAve = new float[maxGen];		// 各代路径的平均长度
	mymath myMath = new mymath();				
	Random rand = new Random();
	
	public void create_eta_Tau(int citysNum) {
		for(int i = 0; i < citysNum; i++) {
			for(int j=0;j<citysNum;j++) {
				Tau[i][j] = 1.0f;
				if(i!=j) {
					eta[i][j] = 1/disMap[i][j];		
				}else {	
					eta[i][j] = 0.0001f;
				}
			}
		}
	}
	
	public void solveTsp() {
		create_eta_Tau(citysNum);
		for(int gen = 0; gen < maxGen; gen++) {
			int[] start = new int[popsize];
			
			for(int i = 0; i < popsize; i++) {
				start[i] = rand.nextInt(citysNum);
				Table[i][0] = start[i];
			}
			
			int[] citys_index = new int[citysNum];
			
			for(int i = 0; i < citysNum; i++) {
				citys_index[i] = i;
			}
			
			for(int i = 0; i< popsize;i++) {
				for (int j = 1; j < citysNum; j++) {
					
					int[] tabu = new int[j];		
					tabu = Arrays.copyOf(Table[i], j);		// 已经访问过的城市
					int[] allow_index = myMath.noismember(citys_index,tabu);		
					int[] allow = new int[allow_index.length];
					
					for(int k = 0;k < allow.length; k++) {
						allow[k] = citys_index[allow_index[k]];
					}										// 待访问城市
					
					// 计算转移城市的概率
					float[] P = new float[allow.length];
					
					for(int k = 0; k < allow.length; k++) {
						P[k] = (float)(Math.pow(Tau[tabu[tabu.length-1]][allow[k]],alpha)
								*Math.pow(eta[tabu[tabu.length-1]][allow[k]],beta));
					}
					
					// 轮盘赌选择下一个城市
					float sumP = myMath.sumfloat(P);
					
					for(int k = 0; k < P.length; k++) {
						P[k] /= sumP;
					}
					
					float[] Pc = myMath.comsumFloat(P);
					float rate = rand.nextFloat();
					int target = 0; 
					
					for(int k = 0; k < Pc.length; k++) {
						
						if(rate <= Pc[k]) {
							target = allow[k];			// 下一个城市
							break;
						}
						
					}
					
					Table[i][j] = target;				// 记录第i个蚂蚁访问的第j个城市
				}
			}
			
			float[] length = new float[popsize];
			
			for(int i = 0; i < popsize; i++) {
				length[i] = calDistance(Table[i]);		// 计算每只蚂蚁所行使的距离
			}
			
			float[] lengthCopy = length.clone();
			
			// 记录第gen次迭代中的最短路线，最短距离，平均距离
			if(gen==0) {
				int[] index = myMath.indexSort(lengthCopy);
				int min_index = index[0];
				float minLength = length[min_index];
				lengthBest[gen] = minLength;			
				lengthAve[gen] = myMath.sumfloat(length)/popsize;
				routeBest[gen] = Table[min_index];
			}else {
				int[] index = myMath.indexSort(lengthCopy);
				int min_index = index[0];
				float minLength = length[min_index];
				lengthAve[gen] = myMath.sumfloat(length)/popsize;
				
				if(minLength <= lengthBest[gen-1]) {
					lengthBest[gen] = minLength;
					routeBest[gen] = Table[min_index];
				}else {
					lengthBest[gen] = lengthBest[gen-1];
					routeBest[gen] = routeBest[gen-1];
				}
				
			}
			
			// 更新信息素
			float[][] deltaTau = new float[citysNum][citysNum];
			
			for(int i = 0; i < popsize; i++) {
				
				for(int j = 0; j < citysNum-1; j++) {
					deltaTau[Table[i][j]][Table[i][j+1]] += (q/length[i]);
				}
				
				deltaTau[Table[i][citysNum-1]][Table[i][0]] += (q/length[i]);
				
			}
			
			for(int i = 0; i < citysNum; i++) {
				
				for(int j = 0; j < citysNum; j++) {
					Tau[i][j] = (1-rho)*Tau[i][j] + deltaTau[i][j];
				}
				
			}
			
			Table = new int[popsize][citysNum];
		}
		
		System.out.println(lengthBest[maxGen-1]);						// 打印全局最短距离
		System.out.println(Arrays.toString(lengthBest));				// 打印各代最短距离
		System.out.println(Arrays.toString(lengthAve));					// 打印各代平均距离
		System.out.println(Arrays.toString(routeBest[maxGen-1]));		// 打印最短路线
		
	}

	public static void main(String[] args) {                                                                                                                                       
		// TODO Auto-generated method stub
		ACO acoTsp = new ACO();	
		acoTsp.solveTsp();
	}
}
