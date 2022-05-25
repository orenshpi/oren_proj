#include <stdio.h>
#include <math.h>
#include <assert.h>
#include <stdlib.h>
int max_iter;
double epsilon=0.001;
int d;
int n;
double norm_oc(double* point1, double* point2);
double** k_means(double** points, int k);
double** calc_centroids(double** centroids, double** points, int k);
double* one_d_arr(int size);
double** two_d_arr(int out_size, int in_size);
int smaller_then_eps(double** centroids, double** new_centroids, int size);
double** loop_calc_centroids(double** centroids, double** points, int k);
double** read_file(char* file_name);
int k_means_outer(int k,char *input_filename,char *out_filename,int max_iteration);
void write_output(double **centroids, int k, char* file_name);
int initialize(int argc , char *argv[]);
int is_number(char *ch);





int main(int argc , char *argv[]) {
    int k;
    int is_working;
    char *input_filename;
    char *out_filename;


    if(initialize(argc,argv) == 1){
        return 1;
    }
    if(argc == 4){
        max_iter = 200;
        input_filename = (char*)argv[2];
        out_filename = argv[3];
    }
    else{
        max_iter = atoi(argv[2]);
        input_filename = (char*)argv[3];
        out_filename = argv[4];
    }
    k = atoi(argv[1]);
    is_working = k_means_outer(k,input_filename,out_filename,max_iter);


    if(is_working == 1){
        return 1;
    }
    return 0;
}




int initialize(int argc , char *argv[]){
    if((argc != 4 && argc != 5) || (is_number(argv[1]) == 1) || (argc == 5 && is_number(argv[2]) == 1)){
        printf("invalid input!");
        return 1;       /* means that the initialize is incorrect */
    }
    else{
        return argc;    /* means that we have 4 or 5 arguments and the initialize is correct */
    }
}


int is_number(char *ch){
    int i = 0;
    while (ch[i] != 0){
        if(ch[i] < 48 || ch[i] > 57){
            return 1;
        }
        i++;
    }
    return 0;
}



int k_means_outer(int k,char *input_filename,char *out_filename,int max_iteration){
    double** input;
    double** cents;
    int vec;
    if(max_iteration<1){
        printf("invalid input!");
        return 1;
    }

    max_iter = max_iteration;
    read_file(input_filename);
    input = two_d_arr(n,d);
    if(k < 1 || k > n){
        printf("invalid input!");
        return 1;
    }

    input= read_file(input_filename);
    cents= two_d_arr(k,d);
    cents= k_means(input, k);
    write_output(cents, k, out_filename);

    for(vec = 0 ; vec < n ; vec++){
        free(input[vec]);
    }

    return 0;
}



double** k_means(double** points, int k){
    double** centroids= two_d_arr(k,d);
    int i;
    double** good_centroids;

    for(i=0; i<k; i++){
        centroids[i]=points[i];
    }
    good_centroids= two_d_arr(k,d);
    good_centroids=loop_calc_centroids(centroids,points, k);
    return  good_centroids;
}



double** loop_calc_centroids(double** centroids, double** points, int k){
    int iter;
    double** new_centroids;
    double** calced_centroids;

    iter = 0;
    new_centroids= two_d_arr(k,d);
    new_centroids = calc_centroids(centroids,points, k);
    while((smaller_then_eps(new_centroids,centroids,k) == 0) & (iter < max_iter)){
        iter+=1;
        calced_centroids= two_d_arr(k,d);
        calced_centroids = calc_centroids(new_centroids,points, k);
        centroids=new_centroids;
        new_centroids=calced_centroids;

    }
    return new_centroids;
}



double** calc_centroids(double** centroids, double** points, int k){
    double** new_centroids;
    double* cent_num_points;
    int i;
    int index_of_close_cent;
    double min_range;
    int j;
    double range;
    int t;
    int z;
    int p;

    new_centroids = two_d_arr(k,d);
    cent_num_points = one_d_arr(k);
    for(i=0; i<n; i++) {
        index_of_close_cent = 0;
        min_range = norm_oc(centroids[0], points[i]);
        for (j = 0; j < k; j++) {
            range = norm_oc(points[i], centroids[j]);
            if (range < min_range) {
                min_range = range;
                index_of_close_cent = j;
            }
        }
        cent_num_points[index_of_close_cent] += 1;
        for (t = 0; t < d; t++) {
            new_centroids[index_of_close_cent][t] += points[i][t];
        }
    }
    for (z = 0; z < k; z++) {
        for (p = 0; p < d; p++) {
            new_centroids[z][p] = new_centroids[z][p] / cent_num_points[z];
        }
    }
    return new_centroids;
}




double** two_d_arr(int out_size, int in_size) {
    double **arr;
    int i;
    double *arr_i;

    arr = malloc(out_size * in_size * sizeof(double));
    if(arr == NULL){
        printf("An Error Has Occurred");
        exit(1);
    }

    for (i = 0; i < out_size; i++) {
        arr_i = one_d_arr(in_size);
        arr[i] = arr_i;
    }
    return arr;
}




double* one_d_arr(int size){
    double *arr;
    int i;

    arr = malloc(size * sizeof(double));
    if(arr == NULL){
        printf("An Error Has Occurred");
        exit(1);
    }
    for(i=0; i<size; i++){
        arr[i]=(double) 0;
    }
    return arr;
}




double norm_oc(double* point1, double* point2) {
    double sum;
    int i;

    sum = 0;
    for (i = 0; i < d; i++) {
        sum += pow((point2[i] - point1[i]), 2);
    }
    return pow(sum, 0.5);
}



int smaller_then_eps(double** centroids, double** new_centroids, int size){
    int i;

    for(i=0; i<size; i++){
        if(norm_oc(centroids[i],new_centroids[i])>epsilon){
            return 0;
        }
    }
    return 1;
}


double** read_file(char* file_name){
    FILE *file;
    double a;
    int total_vec;
    char c;
    int size_vec;
    char ch;
    int num_line;
    int i;
    int j;
    double **p;

    a = 0;
    total_vec = 0;
    size_vec = 0;
    file = fopen(file_name, "r");
    while ((fscanf(file,"%lf",&a) == 1)){
        c = fgetc(file);
        size_vec++;
        if( c == '\n'){
            break;
        }
    }
    fclose(file);

    file = fopen(file_name, "r");
    for(ch = fgetc(file) ; ch != EOF ; ch = fgetc(file)){
        if(ch == '\n'){
            total_vec += 1;
        }
    }
    fclose(file);

    p = malloc(sizeof (double *) * total_vec);
    if(p == NULL){
        printf("An Error Has Occurred");
        exit(1);
    }
    file = fopen(file_name, "r");
    for(num_line = 0 ;num_line <total_vec ; num_line++) {
        p[num_line] = malloc(sizeof(double) * size_vec);
        if(p[num_line] == NULL){
            printf("An Error Has Occurred");
            exit(1);
        }
    }
    for(i = 0 ; i < total_vec ; i++){
        for(j = 0 ; j < size_vec ; j++){
            if(fscanf(file, "%lf", &a) == 1){
                p[i][j] = (double)a;
                fgetc(file);
            }
        }
    }
    fclose(file);
    d=size_vec;
    n=total_vec;
    return p;
}



void write_output(double **centroids, int k, char* file_name){
    FILE *fptr;
    int i;
    int j;

    fptr = fopen(file_name,"w");
    for(i = 0 ; i < k ; i++){
        for(j = 0 ; j < d - 1; j++){
            fprintf(fptr , "%.4f" , centroids[i][j]);
            fprintf(fptr , "%s" , ",");
        }
        fprintf(fptr , "%.4f\n" , centroids[i][d - 1]);
    }
    fclose(fptr);
    free(centroids);
}
