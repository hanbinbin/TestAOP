//
// Created by binbinHan on 2022/11/10.
//
#include <cstring>
#include <cstdlib>
#include "test.h"

//new或者malloc申请的内存是虚拟内存，申请之后不会立即映射到物理内存，即不会占用RAM，只有调用memset使用内存后，虚拟内存才会真正映射到RAM。

void testOOM1() {
    void *p = malloc(1024 * 1024 * 50);
    //memory will not used without calling memset()
    memset(p, 0, 1024 * 1024 * 50);
//    free(p); //free memory
}

void testOOM2() {
    char *p = new char[1024 * 1024 * 50];
    //memory will not used without calling memset()
    memset(p, 1, 1024 * 1024 * 50);
//    free(p); //free memory
}

