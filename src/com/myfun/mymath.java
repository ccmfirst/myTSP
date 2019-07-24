package com.myfun;

public class mymath {
	public float sumfloat(float[] list) {
		float v = 0.0f;
		for(float x:list) {
			v += x;
		}
		return v;
	}
	public float[] comsumFloat(float[] list) {
		float v = 0.0f;
		float[] result = new float[list.length];
		for(int i=0;i<list.length;i++) {
			v += list[i];
			result[i] = v;
		}
		return result;
	}
	
	public int[] indexSort(float[] list) {
		int[] index = new int[list.length];
		for(int k=0;k<list.length;k++) {
			index[k] = k;
		}
		
		for(int i=0;i<list.length-1;i++) {
			boolean flag = false;
			for(int j=1;j<list.length-i;j++) {
				if (list[j-1]>list[j]) {
					float tmp1 = list[j-1];
					int tmp2 = index[j-1];
					list[j-1] = list[j];
					index[j-1] = index[j];
					list[j] = tmp1;
					index[j] = tmp2;
					flag = true;
				}
			}
			if (!flag){
				break;
			}
		
		}		
		return index;
	}
	
}