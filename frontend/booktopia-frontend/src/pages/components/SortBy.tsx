import React, { useState, useEffect } from 'react';

interface SortByProps {
    columns: string[];
    // Callback receiving (algorithm, column, order) when the user clicks "Go"
    onSortChange: (algorithm: string, column: string, order: string) => void;
}

export default function SortBy({ columns, onSortChange }: SortByProps) {
    // Default selections
    const [algorithm, setAlgorithm] = useState("Merge Sort");
    const [column, setColumn] = useState(columns.length > 0 ? columns[0] : "");
    const [order, setOrder] = useState("ASC");

    // When columns are updated, make sure there is a valid default column
    useEffect(() => {
        if (columns.length > 0 && !column) {
            setColumn(columns[0]);
        }
    }, [columns, column]);

    const handleGo = () => {
        onSortChange(algorithm, column, order);
    };

    return (
        <div
            className="new-sortby-container"
            style={{
                display: "flex",
                alignItems: "center",
                gap: "12px",
                padding: "8px",
                marginBottom: "1rem"
            }}
        >
            <div className="new-sortby-algorithm">
                <label htmlFor="new-sortby-algorithm-select" style={{ marginRight: "4px" }}>
                    Sort Algorithm:
                </label>
                <select
                    id="new-sortby-algorithm-select"
                    value={algorithm}
                    onChange={(e) => setAlgorithm(e.target.value)}
                >
                    <option value="Merge Sort">Merge Sort</option>
                    <option value="Heap Sort">Heap Sort</option>
                </select>
            </div>
            <div className="new-sortby-column">
                <label htmlFor="new-sortby-column-select" style={{ marginRight: "4px" }}>
                    Column:
                </label>
                <select
                    id="new-sortby-column-select"
                    value={column}
                    onChange={(e) => setColumn(e.target.value)}
                >
                    {columns.map((col) => (
                        <option key={col} value={col}>
                            {col}
                        </option>
                    ))}
                </select>
            </div>
            <div className="new-sortby-order">
                <label htmlFor="new-sortby-order-select" style={{ marginRight: "4px" }}>
                    Order:
                </label>
                <select
                    id="new-sortby-order-select"
                    value={order}
                    onChange={(e) => setOrder(e.target.value)}
                >
                    <option value="ASC">ASC</option>
                    <option value="DESC">DESC</option>
                </select>
            </div>
            <button
                onClick={handleGo}
                style={{
                    padding: "4px 8px",
                    backgroundColor: "#007bff",
                    color: "#fff",
                    border: "none",
                    borderRadius: "4px",
                    cursor: "pointer"
                }}
            >
                Go
            </button>
        </div>
    );
}
