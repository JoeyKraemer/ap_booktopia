package com.nhlstenden.booktopia.AVL;

public class Node<T>
{
    T key;
    int height;
    Node<T> left;
    Node<T> right;

    Node(T key)
    {
        this.key = key;
        height = 1;
    }
}
