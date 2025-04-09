import React, {useEffect, useState} from 'react';
import SearchBar from "../pages/components/SearchBar";
import SystemMetrics from "../pages/components/SystemMetrics";
import SortBy from "../pages/components/SortBy";
import ItemCard from "../pages/components/ItemCard";
import ButtonGroup from "../pages/components/ButtonGroup";
import AddItemModal from "../pages/components/AddItemModal";
import DeleteConfirmationModal from '../pages/components/DeleteConfirmationModal';


export default function Home() {
    let [items, setItems] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");
    let [sortedItems, setSortedItems] = useState(items);
    const [lastAlgorithm, setLastAlgorithm] = useState("None");
    const [speed, setSpeed] = useState("-");
    let [processingTimeMs, setProcessingTimeMs] = useState(0);
    let [columns, setColumns] = useState([]);
    let [dataStructure, setDataStructure] = useState("None");
    const [isSearching, setIsSearching] = useState(false);
    const [searchMethod, setSearchMethod] = useState("None");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

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
    const handleSearch = (query) => {
        if (!query.trim()) {
            // If search query is empty, reset to original data
            fetch('http://localhost:8080/api/display/table')
                .then(response => response.json())
                .then(data => {
                    if (data && data.data) {
                        setItems(data.data.rows);
                        setSortedItems(data.data.rows);
                        setProcessingTimeMs(data.data.processingTimeMs);
                        setLastAlgorithm("None");
                        setSearchMethod("None");
                    }
                })
                .catch(error => {
                    console.error("Error fetching data:", error);
                });
            return;
        }

        setIsSearching(true);
        setSearchQuery(query);
        
        // Call the backend search endpoint
        fetch(`http://localhost:8080/api/data/search?query=${encodeURIComponent(query)}`)
            .then(response => response.json())
            .then(data => {
                if (data && data.success) {
                    const results = data.results;
                    setItems(results);
                    setSortedItems(results); // Also update sortedItems
                    setProcessingTimeMs(data.processingTimeMs);
                    setLastAlgorithm("Search");
                    setSearchMethod(data.searchMethod || "Unknown");
                } else {
                    console.error("Search failed:", data.error);
                    // Keep the current items if search fails
                }
                setIsSearching(false);
            })
            .catch(error => {
                console.error("Error during search:", error);
                setIsSearching(false);
            });
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

    // Function to handle adding new item
    const handleAddItem = (newItem: any) => {
        fetch('http://localhost:8080/api/data/add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(newItem),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log("Item added:", data);
                setItems((prevItems) => [...prevItems, newItem]); // Update items with new item
            })
            .catch((error) => console.error("Error adding item:", error));
    };

    const handleDeleteConfirm = (key: string) => {
        // Send the delete request to the server
        fetch(`http://localhost:8080/api/data/delete/${key}`, {
            method: 'DELETE',
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.success) {
                    // After successful deletion, remove the item from the items array
                    setItems((prevItems) => prevItems.filter(item => item.key !== key));  // Replace "key" with the correct field
                } else {
                    alert('Error deleting item');
                }
            })
            .catch((error) => {
                console.error("Error deleting item:", error);
            });
    };

    // Handle button actions
    const handleAdd = () => {
        console.log("Add Item clicked");
        setIsModalOpen(true);

    };

    const handleCloseModal = () => {
        setIsModalOpen(false);
    };

    const handleDelete = () => {
        setIsDeleteModalOpen(true);
    };

    const handleCloseDeleteModal = () => {
        setIsDeleteModalOpen(false); // Close the modal without deleting
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
                            searchMethod={searchMethod}
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

                {isModalOpen && (
                    <AddItemModal
                        columns={columns}
                        onClose={handleCloseModal}
                        onSubmit={handleAddItem}
                    />
                )}

                {isDeleteModalOpen && (
                    <DeleteConfirmationModal
                        items={items}  // Pass the items array to the modal
                        onClose={() => setIsDeleteModalOpen(false)}
                        onDelete={handleDeleteConfirm}
                    />
                )}

                <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6 mt-6">
                    {sortedItems.map((item, index) => (
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