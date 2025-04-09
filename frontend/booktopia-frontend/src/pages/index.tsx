import React, { useState } from 'react';
import SearchBar from "../pages/components/SearchBar";
import SystemMetrics from "../pages/components/SystemMetrics";
import SortBy from "../pages/components/SortBy";
import ItemCard from "../pages/components/ItemCard";
import ButtonGroup from "../pages/components/ButtonGroup";

export default function Home() {
    // Example data for items (can be books, movies, songs, etc.)
    const [items, setItems] = useState([
        { title: "Item 1", value1: "A", value2: "B", value3: "C", value4: "D", value5: "E" },
        { title: "Item 2", value1: "F", value2: "G", value3: "H", value4: "I", value5: "J" },
        { title: "Item 3", value1: "K", value2: "L", value3: "M", value4: "N", value5: "O" },
    ]);

    // States for search, sort, and metrics
    const [searchQuery, setSearchQuery] = useState("");
    const [sortedItems, setSortedItems] = useState(items);
    const [lastAlgorithm, setLastAlgorithm] = useState("None");
    const [speed, setSpeed] = useState("-");

    // Handle search functionality
    const handleSearch = (query, string) => {
        setSearchQuery(query);
        const filteredItems = items.filter((item) =>
            item.title.toLowerCase().includes(query.toLowerCase())
        );
        setSortedItems(filteredItems);
    };

    // Handle sorting functionality
    const handleSortChange = (criteria: string, order: "asc" | "desc") => {
        const start = performance.now();

        const sorted = [...sortedItems].sort((a, b) => {
            const valueA = a[criteria as keyof typeof a];
            const valueB = b[criteria as keyof typeof b];

            if (valueA < valueB) return order === "asc" ? -1 : 1;
            if (valueA > valueB) return order === "asc" ? 1 : -1;
            return 0;
        });

        const end = performance.now();
        setSortedItems(sorted);
        setLastAlgorithm("Built-in Sort");
        setSpeed(`${(end - start).toFixed(2)}ms`);
    };

    // Handle button actions
    const handleAdd = () => {
        console.log("Add Item clicked");
    };

    const handleDelete = () => {
        console.log("Delete Item clicked");
    };

    const handleEdit = () => {
        console.log("Edit Item clicked");
    };

    const handleConvert = () => {
        console.log("Convert Data Structure clicked");
    };

    const handleUpload = () => {
        console.log("Upload Data Set clicked");
    };

    return (
        <div className="container">
            <div className="bix-box">
                <div className="top">
                    <div className="search-top">
                        <div className="search-bar">
                            <SearchBar onSearch={handleSearch}/>
                        </div>
                        <div className="sort-by">
                            <h3>Sort By</h3>
                            <SortBy onSortChange={handleSortChange}/>
                        </div>
                    </div>
                    <div className="metrics-box">
                        <SystemMetrics
                            dataStructure="Array"
                            lastAlgorithm={lastAlgorithm}
                            speed={speed}
                        />
                    </div>
                </div>

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mt-6">
                    {sortedItems.map((item) => (
                        <div className="item-card">
                        <ItemCard
                            key={item.title}
                            title={item.title}
                            value1={item.value1}
                            value2={item.value2}
                            value3={item.value3}
                            value4={item.value4}
                            value5={item.value5}
                        />
                    </div>
                    ))}
                </div>

                <div className="button-group">
                    <ButtonGroup
                        onAdd={handleAdd}
                        onDelete={handleDelete}
                        onEdit={handleEdit}
                        onConvert={handleConvert}
                        onUpload={handleUpload}
                    />
                </div>
            </div>
        </div>
    );
}
