import { useState } from "react";

type SortByProps = {
    onSortChange: (criteria: string, order: "asc" | "desc") => void;
};

export default function SortBy({ onSortChange }: SortByProps) {
    const [criteria, setCriteria] = useState("option1");
    const [order, setOrder] = useState<"asc" | "desc">("asc");

    const handleCriteriaChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const newCriteria = e.target.value;
        setCriteria(newCriteria);
        onSortChange(newCriteria, order);
    };

    const handleOrderChange = (e: React.ChangeEvent<HTMLSelectElement>) => {
        const newOrder = e.target.value as "asc" | "desc";
        setOrder(newOrder);
        onSortChange(criteria, newOrder);
    };

    return (
        <div className="flex flex-col sm:flex-row gap-4 w-full max-w-md">
            <select
                value={criteria}
                onChange={handleCriteriaChange}
                className="border p-2 rounded-lg w-full"
            >
                <option value="option1">Option 1</option>
                <option value="option2">Option 2</option>
                <option value="option3">Option 3</option>
                <option value="option4">Option 4</option>
            </select>

            <select
                value={order}
                onChange={handleOrderChange}
                className="border p-2 rounded-lg w-full"
            >
                <option value="asc">Ascending</option>
                <option value="desc">Descending</option>
            </select>
        </div>
    );
}
