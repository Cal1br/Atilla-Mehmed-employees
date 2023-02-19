import React from 'react';
import PairDurationModel from "./model/PairDurationModel";
import DataGrid from 'react-data-grid';
import CSVUpload from "./components/CSVUpload";

import './App.css';
import 'react-data-grid/lib/styles.css'

const columns = [
    {width: 150, key: 'employeeFirstId', name: 'First employee Id'},
    {width: 150, key: 'employeeSecondId', name: 'Second employee Id'},
    {width: 100, key: 'projectId', name: 'Project Id'},
    {width: 300, key: 'duration', name: 'Number of days'},
]


function App() {
    const [rows, setRows] = React.useState<PairDurationModel[]>([]);
    console.log(rows)
    return (
        <div className={'root'}>
            <header className="App-header"></header>
            <main>
                <div style={{padding:10}}>
                    <CSVUpload onCompleteCallback={(rows)=>{
                        setRows(rows);
                    }
                    }/>
                </div>
                <div style={{width:'100%',height:'100%'}}>
                    <DataGrid columns={columns}
                              rows={rows}
                              rowKeyGetter={(row) => ''+row.firstId+row.secondId+row.projectId+row.duration}
                              rowHeight={35}
                              className="fill-grid"
                              direction={'ltr'}
                    />
                </div>
            </main>
        </div>
    );
}

export default App;
