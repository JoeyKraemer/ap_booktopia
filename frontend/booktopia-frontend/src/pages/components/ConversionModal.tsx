import React, { useState } from 'react';

export function ConversionModal({ onClose, onSubmit }) {
    const [targetTree, setTargetTree] = useState('');

    const handleSubmit = (e) => {
        e.preventDefault();
        if (targetTree) {
            onSubmit(targetTree);
        } else {
            alert('Please select a tree type.');
        }
    };

    return (
        <div className="modal-overlay" style={modalOverlayStyle}>
            <div className="modal-content" style={modalContentStyle}>
                <button style={closeButtonStyle} onClick={onClose}>
                    &times;
                </button>
                <h2>Select Tree Conversion</h2>
                <form onSubmit={handleSubmit}>
                    <label>
                        <input
                            type="radio"
                            name="targetTree"
                            value="AVL"
                            onChange={(e) => setTargetTree(e.target.value)}
                        />
                        AVL
                    </label>
                    <br />
                    <label>
                        <input
                            type="radio"
                            name="targetTree"
                            value="BST"
                            onChange={(e) => setTargetTree(e.target.value)}
                        />
                        BST
                    </label>
                    <br />
                    <label>
                        <input
                            type="radio"
                            name="targetTree"
                            value="BTREE"
                            onChange={(e) => setTargetTree(e.target.value)}
                        />
                        BTREE
                    </label>
                    <br />
                    <br />
                    <button type="submit">Convert</button>
                </form>
            </div>
        </div>
    );
}

// Inline styles for simplicity; you can move these to a CSS file.
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
