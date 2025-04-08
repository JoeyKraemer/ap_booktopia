type ItemCardProps = {
    title: string;
    value1: string;
    value2: string;
    value3: string;
    value4: string;
    value5: string;
};

export default function ItemCard({
                                     title,
                                     value1,
                                     value2,
                                     value3,
                                     value4,
                                     value5,
                                 }: ItemCardProps) {
    return (
        <div className="bg-white shadow-md rounded-lg p-4 w-full max-w-sm">
            <h3 className="text-xl font-semibold">{title}</h3>
            <div className="space-y-2 mt-2">
                <div>
                    <span className="font-medium">Value 1:</span> {value1}
                </div>
                <div>
                    <span className="font-medium">Value 2:</span> {value2}
                </div>
                <div>
                    <span className="font-medium">Value 3:</span> {value3}
                </div>
                <div>
                    <span className="font-medium">Value 4:</span> {value4}
                </div>
                <div>
                    <span className="font-medium">Value 5:</span> {value5}
                </div>
            </div>
        </div>
    );
}
