import React, { useEffect, useState } from 'react';
import SearchBar from "../pages/components/SearchBar";
import SystemMetrics from "../pages/components/SystemMetrics";
import SortBy from "../pages/components/SortBy";
import ItemCard from "../pages/components/ItemCard";
import ButtonGroup from "../pages/components/ButtonGroup";
import AddItemModal from "../pages/components/AddItemModal";
import DeleteConfirmationModal from '../pages/components/DeleteConfirmationModal';
import { ConversionModal } from "./components/ConversionModal";
import { UploadModal } from "./components/UploadModal";


export default function Home() {
    const [items, setItems] = useState<any[]>([]);
    const [searchQuery, setSearchQuery] = useState("");
    const [sortedItems, setSortedItems] = useState<any[]>([]);
    const [lastAlgorithm, setLastAlgorithm] = useState("None");
    const [speed, setSpeed] = useState("-");
    const [processingTimeMs, setProcessingTimeMs] = useState(0);
    const [columns, setColumns] = useState<string[]>([]);
    let [dataStructure, setDataStructure] = useState("None");
    const [isSearching, setIsSearching] = useState(false);
    const [searchMethod, setSearchMethod] = useState("None");
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [isDeleteModalOpen, setIsDeleteModalOpen] = useState(false);

    const [showConversionModal, setShowConversionModal] = useState(false);
    const [showUploadModal, setShowUploadModal] = useState(false);

    useEffect(() => {
        fetch('http://localhost:8080/api/display/table')
            .then(response => response.json())
            .then(data => {
                console.log("Fetched data:", data.data);
                if (data && data.data) {
                    setItems(data.data.rows);
                    setColumns(data.data.columns);
                    setProcessingTimeMs(data.data.processingTimeMs);
                    setSortedItems(data.data.rows);
                } else {
                    console.log("No valid array found in the response");
                    setItems([]);
                    setSortedItems([]);
                }
            })
            .catch(error => {
                console.error("Error fetching data:", error);
                setItems([]);
                setSortedItems([]);
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

    const handleConvert = () => {
        setShowConversionModal(true);
    };

    const handleUpload = () => {
        setShowUploadModal(true);
    };

    // Updated conversion handler remains the same
    const submitConversion = (targetTree: string) => {
        // Clear previous data
        setItems([]);
        setSortedItems([]);
        setColumns([]);
        setProcessingTimeMs(0);

        fetch(`http://localhost:8080/api/tree/convert?targetTree=${encodeURIComponent(targetTree)}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' }
        })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    alert('Conversion successful: ' + data.message);
                    refreshDataset();
                } else {
                    alert('Conversion failed: ' + data.error);
                }
            })
            .catch(error => {
                console.error('Error during conversion:', error);
                alert('Error during conversion: ' + error.message);
            })
            .finally(() => {
                setShowConversionModal(false);
            });
    };

    // Updated upload handler remains the same
    const submitUpload = (file: File) => {
        console.log("Uploading CSV file:", file);
        const formData = new FormData();
        formData.append("file", file);
        fetch("http://localhost:8080/api/data/import-csv", {
            method: "POST",
            body: formData
        })
            .then(async response => {
                if (!response.ok) {
                    const text = await response.text();
                    throw new Error(`HTTP ${response.status}: ${text}`);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    alert("Upload successful: " + data.message);
                    refreshDataset();
                } else {
                    alert("Upload failed: " + data.error);
                }
            })
            .catch(error => {
                console.error("Error during CSV upload:", error);
                alert("Error during CSV upload: " + error.message);
            })
            .finally(() => {
                setShowUploadModal(false);
            });
    };

    const handleCustomSort = (algorithm: string, column: string, order: string) => {
        if (algorithm === "Heap Sort") {
            fetch(
                `http://localhost:8080/api/sorting/sort-by-property?property=${encodeURIComponent(column)}&direction=${encodeURIComponent(order)}`,
                { method: "GET" }
            )
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        setSortedItems(data.data);
                        setLastAlgorithm("Heap Sort");
                        setSpeed(`${data.processingTimeMs}ms`);
                    } else {
                        alert("Heap sort failed: " + data.error);
                    }
                })
                .catch(error => {
                    alert("Error during heap sort: " + error.message);
                });
        } else if (algorithm === "Merge Sort") {
            fetch(
                `http://localhost:8080/api/mergeSort/sortByProperty?property=${encodeURIComponent(column)}&direction=${encodeURIComponent(order)}`,
                { method: "GET" }
            )
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        setSortedItems(data.data);
                        setLastAlgorithm("Merge Sort");
                        setSpeed(`${data.processingTimeMs}ms`);
                    } else {
                        alert("Merge sort failed: " + data.error);
                    }
                })
                .catch(error => {
                    alert("Error during merge sort: " + error.message);
                });
        }
    };

    const refreshDataset = () => {
        fetch(`http://localhost:8080/api/display/table?ts=${Date.now()}`)
            .then(response => response.json())
            .then(data => {
                console.log("New fetched data:", data.data);
                if (data && data.data) {
                    setItems(data.data.rows);
                    setColumns(data.data.columns);
                    setProcessingTimeMs(data.data.processingTimeMs);
                    // Reset sortedItems or apply a default sort if needed:
                    setSortedItems(data.data.rows);
                } else {
                    setItems([]);
                    setColumns([]);
                    setProcessingTimeMs(0);
                    setSortedItems([]);
                }
            })
            .catch(error => {
                console.error("Error fetching new data:", error);
                setItems([]);
                setColumns([]);
                setProcessingTimeMs(0);
                setSortedItems([]);
            });
    };

    return (
        <div className="container">
            <div className="bix-box">
                <div className="top">
                    <div className="search-top">
                        <div className="search-bar">
                            <SearchBar onSearch={handleSearch} />
                        </div>
                        <div className="sort-by">
                            <h3>Sort By</h3>
                            <SortBy columns={columns} onSortChange={handleCustomSort} />
                        </div>
                    </div>
                    <div className="metrics-box">
                        <SystemMetrics dataStructure={dataStructure} lastAlgorithm={lastAlgorithm} speed={processingTimeMs} />
                    </div>
                </div>

                <div className="button-group">
                    <ButtonGroup
                        onAdd={handleAdd}
                        onDelete={handleDelete}
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
                    {/* Render sorted items if available; otherwise, fallback to items */}
                    {(sortedItems.length ? sortedItems : items).map((item, index) => (
                        <div className="item-card" key={index}>
                            <ItemCard title={item.title || `Item ${index + 1}`} values={Object.entries(item)} />
                        </div>
                    ))}
                </div>
            </div>

            {showConversionModal && (
                <ConversionModal onClose={() => setShowConversionModal(false)} onSubmit={submitConversion} />
            )}

            {showUploadModal && (
                <UploadModal onClose={() => setShowUploadModal(false)} onSubmit={submitUpload} />
            )}
        </div>
    );
}
