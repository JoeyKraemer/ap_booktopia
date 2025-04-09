import React, { useState } from 'react';

type AddItemModalProps = {
    columns: string[]; // List of columns
    onClose: () => void;
    onSubmit: (newItem: any) => void; // Function to handle item submission
};

export default function AddItemModal({ columns, onClose, onSubmit }: AddItemModalProps) {
    const [formData, setFormData] = useState(
        columns.reduce((acc, column) => {
            acc[column] = ''; // Initialize form fields based on columns
            return acc;
        }, {} as Record<string, string>)
    );

    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>, column: string) => {
        setFormData({ ...formData, [column]: e.target.value });
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        onSubmit(formData); // Pass the form data to the onSubmit function
        onClose(); // Close the modal after submit
    };

    return (
        <div className="modal-overlay" style={modalOverlayStyle}>
            <div className="modal-content" style={modalContentStyle}>
                <button style={closeButtonStyle} onClick={onClose}>
                    &times;
                </button>
                <h2>Add New Item</h2>
                <form onSubmit={handleSubmit}>
                    {columns.map((column, index) => (
                        <div key={index}>
                            <label htmlFor={column}>{column}:</label>
                            <input
                                type="text"
                                id={column}
                                value={formData[column] || ''}
                                onChange={(e) => handleInputChange(e, column)}
                                required
                            />
                        </div>
                    ))}
                    <button type="submit">Add Item</button>
                </form>
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
    overflow: 'auto',
    backgroundColor: 'rgba(0,0,0,0.5)',
};

const modalContentStyle: React.CSSProperties = {
    backgroundColor: '#fff',
    margin: '10% auto',
    padding: '20px',
    border: '1px solid #888',
    width: '30%',
};

const closeButtonStyle: React.CSSProperties = {
    float: 'right',
    fontSize: '20px',
    background: 'none',
    border: 'none',
    cursor: 'pointer',
};
