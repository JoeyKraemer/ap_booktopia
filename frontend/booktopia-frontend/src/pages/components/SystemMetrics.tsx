type SystemMetricsProps = {
    dataStructure: string;
    lastAlgorithm: string;
    speed: number;
    searchMethod?: string;
};

export default function SystemMetrics({
                                          dataStructure,
                                          lastAlgorithm,
                                          speed,
                                          searchMethod = "None",
                                      }: SystemMetricsProps) {
    return (
        <div className="bg-white shadow-md rounded-2xl p-4 w-full max-w-md">
            <h2 className="text-xl font-semibold mb-3">System Metrics</h2>
            <div className="space-y-2">
                <div>
                    <span className="font-medium">Data Structure:</span> {dataStructure}
                </div>
                <div>
                    <span className="font-medium">Last Algorithm:</span> {lastAlgorithm}
                </div>
                {lastAlgorithm === "Search" && (
                    <div>
                        <span className="font-medium">Search Method:</span> {searchMethod}
                    </div>
                )}
                <div>
                    <span className="font-medium">Execution Speed:</span> {speed} ms
                </div>
            </div>
        </div>
    );
}