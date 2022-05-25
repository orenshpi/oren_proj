import sys
import numpy as np
import pandas as pd
import mykmeanssp



def choose_centroids(vec_points, k):

    np.random.seed(0)
    index_arr=[0]*k
    n=len(vec_points)
    vec_cent=[]
    vec_d=[0]*n

    vec_p=[1/n]*len(vec_points)

    rand_num=np.random.choice(np.arange(0, n))
    index_arr[0]=rand_num
    cent=vec_points[rand_num]
    vec_cent.append(cent)
    for t in range(1,k):
        for i in range(n):
            vec_diff = [oc_norm(vec_points[i], vec_cent[j]) for j in range(t)]
            vec_d[i]=min(vec_diff)
        for z in range(n):
            vec_p[z]=vec_d[z]/(sum(vec_d))

        rand_num = np.random.choice(n , p=np.array(vec_p))
        cent=vec_points[rand_num]
        index_arr[t]=rand_num
        vec_cent.append(cent)
    return vec_cent, index_arr

def oc_norm(point1, point2):
    sum_coor = 0
    for t in range(0, len(point1)):
        sum_coor = sum_coor+(point1[t]-point2[t])**2
    return sum_coor


def create_txt(txt_1, txt_2):

    text1 = np.genfromtxt(txt_1, delimiter=",")
    text2 = np.genfromtxt(txt_2, delimiter=",")
    text1 = pd.DataFrame(data=text1[0:,1:],index=text1[0:,0])
    text2 = pd.DataFrame(data=text2[0:,1:],index=text2[0:,0])
    lines = pd.merge(text1,text2,how="inner",left_index=True,right_index=True)
    lines.index = lines.index.astype(int)
    lines = lines.sort_index()
    return lines






def main(k, epsilon, input_text1, input_text2, max_iter=300):
    lines = create_txt(input_text1, input_text2)

    lines=lines.to_numpy().tolist()

    chosen_centroids, index_arr=choose_centroids(lines,k)

    ret_centroids = mykmeanssp.fit(lines, chosen_centroids, max_iter)

    py_ret_centroids = np.array(ret_centroids)

    output_index = ""
    for i in range(len(index_arr)):
        output_index += str(index_arr[i])
        if i != len(index_arr) - 1:
                output_index += ','
    print(output_index)

    output_centroids = ""
    for i in range(py_ret_centroids.shape[0]):
        for j in range(py_ret_centroids.shape[1]):
            output_centroids += str(np.round(py_ret_centroids[i][j], 4))
            if j != (py_ret_centroids.shape[1] - 1):
                output_centroids += ','
        output_centroids += '\n'
    print(output_centroids)


    return 0



array = sys.argv
def is_not_num(x):
    for i in range(len(x)):
        if not x[i].isdigit():
            return 1
if len(array) != 5 and len(array) != 6:
    print("invalid input!")
    exit(1)


if is_not_num(array[1])==1:
    print("invalid input!")
    exit(1)

if int(array[1])<=0:
    print("invalid input!")
    exit(1)

if len(array) == 6:
    if is_not_num(array[2]) == 1:
        print("invalid input!")
        exit(1)
    if int(array[2])<=0:
        print("invalid input!")
        exit(1)

    main(int(array[1]), float(array[3]),array[4], array[5], int(array[2]))
else:
    main(int(array[1]), float(array[2]),array[3], array[4])




