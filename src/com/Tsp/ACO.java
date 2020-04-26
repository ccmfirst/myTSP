package com.Tsp;

import java.util.Arrays;
import java.util.Random;

import com.myfun.mymath;

public class ACO extends TspData {

	final int popsize = 50; // ��Ⱥ��С
	final int maxGen = 200; // ����������
	final float alpha = 1.0f; // ��Ϣ����Ҫ�̶�����
	final float beta = 5.0f; // ����������Ҫ�̶�����
	final float rho = 0.1f; // ��Ϣ�ػӷ�����
	final float q = 1.0f; // ��ϵ��
	float[][] eta = new float[citysNum][citysNum]; // ��������
	float[][] Tau = new float[citysNum][citysNum]; // ��Ϣ�ؾ���
	int[][] Table = new int[popsize][citysNum]; // ·����¼��
	int[][] routeBest = new int[maxGen][citysNum]; // �������·��
	float[] lengthBest = new float[maxGen]; // �������·���ĳ���
	float[] lengthAve = new float[maxGen]; // ����·����ƽ������
	mymath myMath = new mymath();
	Random rand = new Random();

	public void create_eta_Tau(int citysNum) {
		for (int i = 0; i < citysNum; i++) {
			for (int j = 0; j < citysNum; j++) {
				Tau[i][j] = 1.0f;
				if (i != j) {
					eta[i][j] = 1 / disMap[i][j];
				} else {
					eta[i][j] = 0.0001f;
				}
			}
		}
	}

	public void solveTsp() {
		create_eta_Tau(citysNum);
		for (int gen = 0; gen < maxGen; gen++) {
			int[] start = new int[popsize];

			for (int i = 0; i < popsize; i++) {
				start[i] = rand.nextInt(citysNum);
				Table[i][0] = start[i];
			}

			int[] citys_index = new int[citysNum];

			for (int i = 0; i < citysNum; i++) {
				citys_index[i] = i;
			}

			for (int i = 0; i < popsize; i++) {
				for (int j = 1; j < citysNum; j++) {

					int[] tabu = new int[j];
					tabu = Arrays.copyOf(Table[i], j); // �Ѿ����ʹ��ĳ���
					int[] allow_index = myMath.noismember(citys_index, tabu);
					int[] allow = new int[allow_index.length];

					for (int k = 0; k < allow.length; k++) {
						allow[k] = citys_index[allow_index[k]];
					} // �����ʳ���

					// ����ת�Ƴ��еĸ���
					float[] P = new float[allow.length];

					for (int k = 0; k < allow.length; k++) {
						P[k] = (float) (Math.pow(Tau[tabu[tabu.length - 1]][allow[k]], alpha)
								* Math.pow(eta[tabu[tabu.length - 1]][allow[k]], beta));
					}

					// ���̶�ѡ����һ������
					float sumP = myMath.sumfloat(P);

					for (int k = 0; k < P.length; k++) {
						P[k] /= sumP;
					}

					float[] Pc = myMath.comsumFloat(P);
					float rate = rand.nextFloat();
					int target = 0;

					for (int k = 0; k < Pc.length; k++) {

						if (rate <= Pc[k]) {
							target = allow[k]; // ��һ������
							break;
						}

					}

					Table[i][j] = target; // ��¼��i�����Ϸ��ʵĵ�j������
				}
			}

			float[] length = new float[popsize];

			for (int i = 0; i < popsize; i++) {
				length[i] = calDistance(Table[i]); // ����ÿֻ��������ʹ�ľ���
			}

			float[] lengthCopy = length.clone();

			// ��¼��gen�ε����е����·�ߣ���̾��룬ƽ������
			if (gen == 0) {
				int[] index = myMath.indexSort(lengthCopy);
				int min_index = index[0];
				float minLength = length[min_index];
				lengthBest[gen] = minLength;
				lengthAve[gen] = myMath.sumfloat(length) / popsize;
				routeBest[gen] = Table[min_index];
			} else {
				int[] index = myMath.indexSort(lengthCopy);
				int min_index = index[0];
				float minLength = length[min_index];
				lengthAve[gen] = myMath.sumfloat(length) / popsize;

				if (minLength <= lengthBest[gen - 1]) {
					lengthBest[gen] = minLength;
					routeBest[gen] = Table[min_index];
				} else {
					lengthBest[gen] = lengthBest[gen - 1];
					routeBest[gen] = routeBest[gen - 1];
				}

			}

			// ������Ϣ��
			float[][] deltaTau = new float[citysNum][citysNum];

			for (int i = 0; i < popsize; i++) {

				for (int j = 0; j < citysNum - 1; j++) {
					deltaTau[Table[i][j]][Table[i][j + 1]] += (q / length[i]);
				}

				deltaTau[Table[i][citysNum - 1]][Table[i][0]] += (q / length[i]);

			}

			for (int i = 0; i < citysNum; i++) {

				for (int j = 0; j < citysNum; j++) {
					Tau[i][j] = (1 - rho) * Tau[i][j] + deltaTau[i][j];
				}

			}

			Table = new int[popsize][citysNum];
		}

		System.out.println(lengthBest[maxGen - 1]); // ��ӡȫ����̾���
		System.out.println(Arrays.toString(lengthBest)); // ��ӡ������̾���
		System.out.println(Arrays.toString(lengthAve)); // ��ӡ����ƽ������
		System.out.println(Arrays.toString(routeBest[maxGen - 1])); // ��ӡ���·��

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ACO acoTsp = new ACO();
		acoTsp.solveTsp();
	}
}
