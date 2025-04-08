type SearchBarProps = {
    onSearch: (query: string) => void;
};

export default function SearchBar({ onSearch }: SearchBarProps) {
    const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        onSearch(e.target.value);
    };

    return (
        <input
            type="text"
            placeholder="Search..."
            onChange={handleInputChange}
            className="border px-4 py-2 rounded-xl w-full"
        />
    );
}
