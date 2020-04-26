package com.myfun;

import java.util.Arrays;
import java.util.Random;

public class mysort {
	public void bubbleSort(int[] list) {
		int n = list.length;
		for (int i = 0; i < n - 1; i++) {
			boolean flag = false;
			for (int j = 1; j < n - i; j++) {
				if (list[j - 1] > list[j]) {
					int tmp = list[j - 1];
					list[j - 1] = list[j];
					list[j] = tmp;
					flag = true;
				}
			}
			if (!flag) {
				break;
			}
		}
	}

	public void selectionSort(int[] list) {
		int n = list.length;
		for (int i = 0; i < n; i++) {
			int min = i;
			for (int j = i; j < n; j++) {
				if (list[j] < list[min]) {
					min = j;
				}
			}
			if (min != i) {
				int tmp = list[i];
				list[i] = list[min];
				list[min] = tmp;
			}
		}
	}

	public void insertSort(int[] list) {
		int n = list.length;
		for (int i = 1; i < n; i++) {
			for (int j = i; j > 0; j--) {
				if (list[j] < list[j - 1]) {
					int tmp = list[j];
					list[j] = list[j - 1];
					list[j - 1] = tmp;
				}
			}
		}
	}

	public void shellSort(int[] list) {
		int len = list.length;
		int temp, gap = len / 2;
		while (gap > 0) {
			for (int i = gap; i < len; i++) {
				temp = list[i];
				int preIndex = i - gap;
				while (preIndex >= 0 && list[preIndex] > temp) {
					list[preIndex + gap] = list[preIndex];
					preIndex -= gap;
				}
				list[preIndex + gap] = temp;
			}
			gap /= 2;
		}
	}

	public static int[] mergeSort(int[] array) {
		if (array.length < 2) {
			return array;
		}
		int mid = array.length / 2;
		int[] left = Arrays.copyOf(array, mid);
		int[] right = Arrays.copyOfRange(array, mid, array.length);
		return merge(mergeSort(left), mergeSort(right));
	}

	public static int[] merge(int[] left, int[] right) {
		int[] result = new int[left.length + right.length];
		for (int index = 0, i = 0, j = 0; index < result.length; index++) {
			if (i >= left.length) {
				result[index] = right[j++];
			} else if (j >= right.length) {
				result[index] = left[i++];
			} else if (left[i] >= right[j]) {
				result[index] = right[j++];
			} else
				result[index] = left[i++];
		}
		return result;
	}

	public int[] quickSort(int[] array, int start, int end) {
		if (array.length < 1 || start < 0 || end >= array.length || start > end) {
			return null;
		}
		int smallIndex = partition(array, start, end);
		if (smallIndex > start) {
			quickSort(array, start, smallIndex - 1);
		}

		if (smallIndex < end) {
			quickSort(array, smallIndex + 1, end);
		}
		return array;
	}

	public int partition(int[] array, int start, int end) {
		int pivot = (int) (start + Math.random() * (end - start + 1));
		int smallIndex = start - 1;
		swap(array, pivot, end);
		for (int i = start; i <= end; i++) {
			if (array[i] <= array[end]) {
				smallIndex++;
				if (i > smallIndex) {
					swap(array, i, smallIndex);
				}
			}
		}
		return smallIndex;
	}

	public void swap(int[] array, int i, int j) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
	}

	public static void main(String[] args) {
		mysort mysort = new mysort();
		Random rand = new Random();
		int[] list = new int[10];
		for (int i = 0; i < 10; i++) {
			list[i] = i;
		}
		for (int i = 0; i < 10; i++) {
			int j = rand.nextInt(10 - i);
			int tmp = list[i];
			list[i] = list[j];
			list[j] = tmp;
		}
		System.out.println(Arrays.toString(list));
		list = mysort.quickSort(list, 0, list.length - 1);
		System.out.println(Arrays.toString(list));
	}

}
