import React, {useEffect, useState} from 'react';
import SearchBar from "../pages/components/SearchBar";
import SystemMetrics from "../pages/components/SystemMetrics";
import SortBy from "../pages/components/SortBy";
import ItemCard from "../pages/components/ItemCard";
import ButtonGroup from "../pages/components/ButtonGroup";

export default function Home() {
    let [items, setItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    let [sortedItems, setSortedItems] = useState(items);
    const [lastAlgorithm, setLastAlgorithm] = useState("None");
    const [speed, setSpeed] = useState("-");
    let [processingTimeMs, setProcessingTimeMs] = useState(0);
    let [columns, setColumns] = useState([]);
    let [dataStructure, setDataStructure] = useState("None");

    useEffect(() => {
        fetch('http://localhost:8080/api/display/table')
            .then(response => response.json())
            .then(data => {
                console.log("Fetched data:", data.data);
                if (data && data.data) {
                    setItems(data.data.rows);
                    setColumns(data.data.columns);
                    setProcessingTimeMs(data.data.processingTimeMs);

                    handleSortChange('title', 'asc'); // Sort by title, ascending
                } else {
                    console.log("No valid array found in the response");
                    setItems([]);  // Fallback to empty array if the data isn't what we expect
                }
            })
            .catch(error => {
                console.error("Error fetching data:", error);
                setItems([]);  // Fallback to empty array on error
            });
    }, []);

    useEffect(() => {
        fetch('http://localhost:8080/api/tree/current')
            .then(response => response.json())
            .then(data => {
                if (data) {
                    setDataStructure(data.treeType);
                } else {
                    setDataStructure('None');  // Fallback to empty array if the data isn't what we expect
                }
            })
            .catch(error => {
                setDataStructure('None');  // Fallback to empty array on error
            });
    }, []);

    console.log(dataStructure);

    // Handle search functionality
    const handleSearch = (query, string) => {
        setSearchQuery(query);
    };

    // Handle sorting functionality
    const handleSortChange = (criteria: string, order: "asc" | "desc") => {
        const start = performance.now();

        const sorted = [...items].sort((a, b) => {
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
                            dataStructure={dataStructure}
                            lastAlgorithm={lastAlgorithm}
                            speed={processingTimeMs}
                        />
                    </div>
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

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mt-6">
                    {items.map((item, index) => (
                        <div className="item-card" key={index}>
                            <ItemCard
                                title={item.title || item.name || `Item ${index + 1}`} // check for both title and name fields
                                values={Object.entries(item)} // Pass all key-value pairs
                            />
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}