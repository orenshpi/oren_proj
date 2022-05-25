import sys


def main(k, input_text, output_text, max_iter=200):
    text_file = open(input_text, "r")
    lines = text_file.readlines()

    for i in range(len(lines)):
        lines[i] = lines[i].split(",")
        for j in range(len(lines[i])):
            lines[i][j] = float(lines[i][j])
    text_file.close()
    f = open(output_text, "a")

    if (k > len(lines)):
        print("invalid input!")
        return 1

    cent_list = (k_means(lines, k, 0.001, max_iter))

    cent_list = round4(cent_list)
    str_cent = ""
    for i in range(len(cent_list)):
        for j in range(len(cent_list[i])):

            str_cent = str_cent + (str(cent_list[i][j]))
            if j != len(cent_list[i]) - 1:
                str_cent = str_cent + ","
        str_cent = str_cent + "\n"
    f.write(str_cent)
    f.close()
    return 0


def k_means(vec_points, num_clust, epsilon=0.001, max_iter=200):
    points_dict = {}
    for t in vec_points:
        points_dict[tuple(t)] = []

    vec_centroids = vec_points[0:num_clust]
    centroids_dict = {}
    for t in vec_centroids:
        centroids_dict[tuple(t)] = []
    iteration_number = 0
    smaller_then_eps = False
    for point in vec_points:
        centroids_dict[tuple(assign_to_clust(point, vec_centroids, points_dict))].append(point)
    while (not smaller_then_eps) and iteration_number != max_iter:
        iteration_number = iteration_number+1

        for point in vec_points:
            remove_from_clust(centroids_dict, tuple(points_dict[tuple(point)]), point)
            centroids_dict[tuple(assign_to_clust(point, vec_centroids, points_dict))].append(point)

        centroids_dict, smaller_then_eps, vec_centroids = calc_new_centroids(centroids_dict, epsilon, points_dict)

    return vec_centroids


def calc_new_centroids(centroids_dict, epsilon, points_dict):
    new_vec_centroids = []
    smaller_than_eps = True
    new_centroids_dict = {}
    for key in centroids_dict:
        new_centroid = calc_new_centroid(key, centroids_dict[key], points_dict)
        if (oc_norm(key, new_centroid)) >= epsilon:
            smaller_than_eps = False
        new_centroids_dict[new_centroid] = centroids_dict[key]
        new_vec_centroids.append(new_centroid)

    return new_centroids_dict, smaller_than_eps, new_vec_centroids


def remove_from_clust(centroids_dict, centroid, point):
    cluster = centroids_dict[centroid]
    for t in cluster:
        if point == t:
            cluster.remove(t)


def calc_new_centroid(centroid, clust_points, points_dict):
    new_centroid = [0]*len(centroid)
    for coordinate in range(len(centroid)):
        sum_coordinate = 0
        for point in clust_points:
            sum_coordinate = sum_coordinate+point[coordinate]

        new_centroid[coordinate] = sum_coordinate/len(clust_points)
    for point in clust_points:
        points_dict[tuple(point)] = new_centroid

    return tuple(new_centroid)


def oc_norm(point1, point2):
    sum_coor = 0
    for t in range(0, len(point1)):
        sum_coor = sum_coor+(point1[t]-point2[t])**2
    return sum_coor** 0.5


def assign_to_clust(point, vec_centroids, points_dict):
    close_cent = vec_centroids[0]
    min_range = oc_norm(point, vec_centroids[0])
    for t in range(len(vec_centroids)):
        norm_range = oc_norm(point, vec_centroids[t])
        if norm_range < min_range:
            min_range = norm_range
            close_cent = vec_centroids[t]
    points_dict[tuple(point)] = tuple(close_cent)

    return close_cent




def round4(cents_list):
    new_cents_list=[]
    for i in range(len(cents_list)):
        cent_list=[]
        for j in range(len(cents_list[i])):
            cent_list.append(format(cents_list[i][j], '.4f'))
        cent_list_tup=tuple(cent_list)
        new_cents_list.append(cent_list_tup)
    return new_cents_list






array = sys.argv
def is_num(x):
    for i in range(len(x)):
        if not x[i].isdigit():
            return 1
if len(array) != 4 and len(array) != 5:
    print("invalid input!")
    exit(1)


if is_num(array[1])==1:
    print("invalid input!")
    exit(1)

if int(array[1])<=0:
    print("invalid input!")
    exit(1)

if len(array) == 5:
    if is_num(array[2]) == 1:
        print("invalid input!")
        exit(1)
    if int(array[2])<=0:
        print("invalid input!")
        exit(1)

    main(int(array[1]),array[3], array[4], int(array[2]))
else:
    main(int(array[1]), array[2], array[3])










