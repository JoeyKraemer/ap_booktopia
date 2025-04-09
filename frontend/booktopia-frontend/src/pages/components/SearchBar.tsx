type SearchBarProps = {
    onSearch: (query: string) => void;
};

export default function SearchBar({ onSearch }: SearchBarProps) {
    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        const form = e.target as HTMLFormElement;
        const input = form.elements.namedItem('searchInput') as HTMLInputElement;
        onSearch(input.value);
    };

    return (
        <form onSubmit={handleSubmit} className="flex w-full">
            <input
                type="text"
                name="searchInput"
                placeholder="Search..."
                className="border px-4 py-2 rounded-l-xl w-full"
            />
            <button 
                type="submit" 
                className="bg-blue-500 text-white px-4 py-2 rounded-r-xl hover:bg-blue-600"
            >
                Search
            </button>
        </form>
    );
}
