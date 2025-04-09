import React, { useState } from 'react';

type DeleteConfirmationModalProps = {
    items: any[];
    onClose: () => void;
    onDelete: (key: string) => void;  // Use key as parameter for deletion
};

export default function DeleteConfirmationModal({
                                                    items,
                                                    onClose,
                                                    onDelete,
                                                }: DeleteConfirmationModalProps) {
    const [inputKey, setInputKey] = useState('');
    const [error, setError] = useState<string | null>(null);

    const handleDelete = () => {
        // Check if the key exists in the items
        const itemToDelete = items.find(item => item.key === inputKey);  // Replace "key" with the correct property
        if (itemToDelete) {
            onDelete(inputKey);  // Call the onDelete function with the key of the item to delete
            onClose();  // Close the modal after deletion
        } else {
            setError('Item not found. Please check the key and try again.');
        }
    };

    return (
        <div className="modal-overlay" style={modalOverlayStyle}>
            <div className="modal-content" style={modalContentStyle}>
                <button style={closeButtonStyle} onClick={onClose}>
                    &times;
                </button>
                <h2>Delete Item</h2>
                <p>Please enter the key of the item you want to delete:</p>
                <div>
                    <input
                        type="text"
                        value={inputKey}
                        onChange={(e) => setInputKey(e.target.value)}
                        placeholder="Enter item key"
                        className="border p-2"
                    />
                </div>
                {error && <p className="text-red-600">{error}</p>} {/* Show error message if item is not found */}
                <div className="mt-4">
                    <button onClick={handleDelete} className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700">
                        Delete Item
                    </button>
                </div>
            </div>
        </div>
    );
}

const modalOverlayStyle: React.CSSProperties = {
    position: 'fixed',
    zIndex: 1000,
    left: 0,
    top: 0,
    width: '100%',
    height: '100%',
    backgroundColor: 'rgba(0, 0, 0, 0.5)',
};

const modalContentStyle: React.CSSProperties = {
    backgroundColor: '#fff',
    margin: '10% auto',
    padding: '20px',
    width: '30%',
    border: '1px solid #888',
};

const closeButtonStyle: React.CSSProperties = {
    float: 'right',
    fontSize: '20px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
};
