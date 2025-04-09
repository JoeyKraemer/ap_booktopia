import { useState, useEffect, useCallback } from 'react';

export function useTreeData() {
    const [dataStructure, setDataStructure] = useState<string>("None");

    const fetchTreeData = useCallback(() => {
        fetch('http://localhost:8080/api/tree/current')
            .then((response) => response.json())
            .then((data) => {
                if (data) {
                    setDataStructure(data.treeType);
                } else {
                    setDataStructure("None");
                }
            })
            .catch((error) => {
                console.error("Error fetching tree data:", error);
                setDataStructure("None");
            });
    }, []);

    useEffect(() => {
        fetchTreeData();
    }, [fetchTreeData]);

    return { dataStructure, fetchTreeData };
}
