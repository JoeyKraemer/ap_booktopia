type ButtonGroupProps = {
    onAdd: () => void;
    onDelete: () => void;
    onConvert: () => void;
    onUpload: () => void;
};

export default function ButtonGroup({
                                        onAdd,
                                        onDelete,
                                        onConvert,
                                        onUpload,
                                    }: ButtonGroupProps) {
    return (
        <div className="flex space-x-4">
            <button
                onClick={onAdd}
                className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 transition"
            >
                Add Item
            </button>
            <button
                onClick={onDelete}
                className="bg-red-600 text-white px-4 py-2 rounded-lg hover:bg-red-700 transition"
            >
                Delete Item
            </button>
            <button
                onClick={onConvert}
                className="bg-blue-600 text-white px-4 py-2 rounded-lg hover:bg-blue-700 transition"
            >
                Convert Data Structure
            </button>
            <button
                onClick={onUpload}
                className="bg-purple-600 text-white px-4 py-2 rounded-lg hover:bg-purple-700 transition"
            >
                Upload Data Set
            </button>
        </div>
    );
}
