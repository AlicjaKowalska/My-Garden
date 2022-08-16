# <h1 align="center">My Garden</h1>

<p align="center">Plant care scheduler for Android</p>

#

**Features:**

• adding a plant to the plant list 

• editing a plant from the plant list

• removing a plant from the plant list

<img src="https://i.imgur.com/o52phKc.png" width=50% height=50%><img src="https://i.imgur.com/sRiojRF.png" width=50% height=50%><img src="https://i.imgur.com/y0C1e8u.png" width=50% height=50%>

• changing the frequency of watering the plant

• changing the frequency of fertilizing the plant

• changing the frequency of transplanting the plant

<img src="https://i.imgur.com/9ZNap5p.png" width=50% height=50%><img src="https://i.imgur.com/Qe4V4VT.png" width=50% height=50%>

• selecting location of the plant using a light sensor,

<img src="https://i.imgur.com/KpTVTn7.png" width=50% height=50%><img src="https://i.imgur.com/kqqMLkg.png" width=50% height=50%>

• marking a task and removing it from the task list

<img src="https://i.imgur.com/stxD9G0.png" width=50% height=50%>








#include <stdio.h>
#include <stdlib.h>

void merge(int *arr, int l, int middle, int r)
{
    int i, j, k;
    int size_l = middle - l + 1;
    int size_r = r - middle;

    /* create temp arrays */
    int left_half[size_l], right_half[size_r];

    /* Copy data to temp arrays L[] and R[] */
    for (i = 0; i < size_l; i++)
        left_half[i] = *(arr+l+i);
    for (j = 0; j < size_r; j++)
        right_half[j] = *(arr+middle+1+j);

    /* Merge the temp arrays back into arr[l..r]*/
    i = 0; // Initial index of first subarray
    j = 0; // Initial index of second subarray
    k = l; // Initial index of merged subarray

    while (i < size_l && j < size_r) {
        if (left_half[i] <= right_half[j]) {
            *(arr+k)= left_half[i];
            i++;
        }
        else {
            *(arr+k)= right_half[j];
            j++;
        }
        k++;
    }

    /* Copy the remaining elements of L[], if there
    are any */
    while (i < size_l) {
        *(arr+k) = left_half[i];
        i++;
        k++;
    }

    /* Copy the remaining elements of R[], if there
    are any */
    while (j < size_r) {
        *(arr+k) = right_half[j];
        j++;
        k++;
    }
}

void mergeSort(int *arr, int l, int r)

{
    if (l < r) {
        int middle = l + (r - l) / 2;

        mergeSort(arr, l, middle);
        mergeSort(arr, middle + 1, r);
        merge(arr, l, middle, r);
    }
}

void printArray(int *arr, int size)

{
    int i;

    for (i = 0; i < size; i++)
        printf("%d ", *(arr+i));

    printf("\n");
}

int main()
{
    int arr[] = { 22, 6, -4, 74, 0, 5, 28, 11, 2, 0};
    int arr_size = sizeof(arr) / sizeof(arr[0]);

    printArray(arr, arr_size);

    mergeSort(arr, 0, arr_size - 1);

    printArray(arr, arr_size);
    return 0;
}
