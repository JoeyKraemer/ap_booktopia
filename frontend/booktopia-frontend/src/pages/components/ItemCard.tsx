type ItemCardProps = {
    title: string;
    values: [string, any][];
};

export default function ItemCard({ title, values }: ItemCardProps) {
    return (
        <div className="bg-white shadow-md rounded-lg p-4 w-full max-w-sm">
            <h3 className="text-xl font-semibold">{title}</h3>
            <div className="space-y-2 mt-2">
                {values
                    .filter(([key, _value]) => key !== "title")
                    .map(([key, value], index) => (
                        <div key={index}>
                            <span className="font-medium">{key}:</span> {String(value)}
                        </div>
                    ))}
            </div>
        </div>
    );
}
