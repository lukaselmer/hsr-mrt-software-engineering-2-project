class CreateGpsPositions < ActiveRecord::Migration
  def self.up
    create_table :gps_positions do |t|
      t.decimal :latitude, :precision => 15, :scale => 10
      t.decimal :longitude, :precision => 15, :scale => 10

      #t.datetime :time
      t.timestamps
    end
  end

  def self.down
    drop_table :gps_positions
  end
end
