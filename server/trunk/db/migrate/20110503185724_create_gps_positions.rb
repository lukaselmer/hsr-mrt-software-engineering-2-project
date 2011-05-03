class CreateGpsPositions < ActiveRecord::Migration
  def self.up
    create_table :gps_positions do |t|
      t.float :latitude
      t.float :longitude

      #t.datetime :time
      t.timestamps
    end
  end

  def self.down
    drop_table :gps_positions
  end
end
