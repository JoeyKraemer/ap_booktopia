import React, { useState } from 'react';

export function UploadModal({ onClose, onSubmit }: { onClose: () => void; onSubmit: (file: File) => void; }) {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files && e.target.files[0];
        if (file) {
            setSelectedFile(file);
        }
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (selectedFile) {
            onSubmit(selectedFile);
        } else {
            alert('Please select a CSV file.');
        }
    };

    return (
        <div className="modal-overlay" style={modalOverlayStyle}>
            <div className="modal-content" style={modalContentStyle}>
                <button style={closeButtonStyle} onClick={onClose}>
                    &times;
                </button>
                <h2>Upload CSV Data</h2>
                <form onSubmit={handleSubmit}>
                    <div>
                        <label htmlFor="fileInput">Select CSV File:</label>
                        <input
                            type="file"
                            id="fileInput"
                            accept=".csv"
                            onChange={handleFileChange}
                            required
                        />
                    </div>
                    <br />
                    <button type="submit">Upload</button>
                </form>
            </div>
        </div>
    );
}

// Inline styles (you can also move these to a CSS file)
const modalOverlayStyle: React.CSSProperties = {
    position: 'fixed',
    zIndex: 1000,
    left: 0,
    top: 0,
    width: '100%',
    height: '100%',
    overflow: 'auto',
    backgroundColor: 'rgba(0,0,0,0.5)'
};

const modalContentStyle: React.CSSProperties = {
    backgroundColor: '#fff',
    margin: '10% auto',
    padding: '20px',
    border: '1px solid #888',
    width: '30%'
};

const closeButtonStyle: React.CSSProperties = {
    float: 'right',
    fontSize: '20px',
    background: 'none',
    border: 'none',
    cursor: 'pointer'
};
